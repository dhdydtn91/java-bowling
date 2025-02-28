package bowling.domain;

import bowling.dto.ScoreDto;

public class BowlingFinalFrame extends BowlingFrame {

    private static final int FINAL_ROUND = 10;

    private Score score;
    private BonusScore bonusScore;


    private BowlingFinalFrame(FinalRound round, Score score, BonusScore bonusScore, FramePoint framePoint) {
        super(round, framePoint);
        this.score = score;
        this.bonusScore = bonusScore;
    }

    public static BowlingFinalFrame first(FinalRound round, FramePoint framePoint) {
        return new BowlingFinalFrame(round, Score.initScore(), BonusScore.empty(), framePoint);
    }

    public static BowlingFrame of(FinalRound round, Score score) {
        return new BowlingFinalFrame(round, score, BonusScore.empty(), FramePoint.first());
    }

    public static BowlingFrame of(FinalRound round, Score score, BonusScore bonusScore) {
        return new BowlingFinalFrame(round, score, bonusScore, FramePoint.first());
    }

    @Override
    public BowlingFrame secondPitching(Point point) {
        score = score.next(point);
        return this;
    }

    @Override
    public BowlingFrame firstPitching(Point point) {
        score = Score.first(point);
        return this;
    }

    @Override
    public BowlingFrame bonusPitching(Point point) {
        bonusScore = BonusScore.of(FinalRound.of(), score, point);
        return this;
    }

    @Override
    public BowlingRole isType() {
        return score.type();
    }

    @Override
    public ScoreDto toDto() {
        return ScoreDto.of(score, bonusScore);
    }

    @Override
    public FramePoint calculateOfScore() {
        if (score.type() == BowlingRole.SPARE || score.type() == BowlingRole.STRIKE) {
            return FramePoint.of(framePoint().toInt() + this.score.total() + bonusScore.point());
        }
        return FramePoint.of(framePoint().toInt() + score.total());
    }

    @Override
    public Score score() {
        return score;
    }
}
