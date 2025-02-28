package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Question extends AbstractEntity {
    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers;

    private boolean deleted = false;

    public Question() {
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Question(long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }

    public Question(long id, String title, String contents, Answers answers) {
        super(id);
        this.title = title;
        this.contents = contents;
        this.answers = answers;
    }

    public User getWriter() {
        return writer;
    }

    public Question writeBy(User loginUser) {
        this.writer = loginUser;
        return this;
    }

    public void addAnswer(Answer answer) {
        this.answers = Answers.of();
        answer.toQuestion(this);
        answers.add(answer);
    }

    public boolean isOwner(User loginUser) {
        if(!writer.equals(loginUser)){
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        return Optional.ofNullable(answers)
        .map(answer ->answer.isOwner(loginUser))
        .orElse(true);
    }

    public DeleteHistorys delete() {
        this.deleted = true;

        List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        deleteHistoryList.add(new DeleteHistory(ContentType.QUESTION, getId(),getWriter(), LocalDateTime.now()));

        Answers answers = Optional.ofNullable(this.answers)
                .orElseGet(Answers::of);

        return answers.delete(deleteHistoryList);
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }


}
