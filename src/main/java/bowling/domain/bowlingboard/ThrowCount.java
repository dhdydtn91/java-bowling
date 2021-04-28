package bowling.domain.bowlingboard;

import bowling.domain.frame.round.Round;

import java.util.Objects;

public class ThrowCount {

    public static final int MIN_THROW_COUNT = 0;
    public static final int MAX_THROW_COUNT = 3;
    public static final int FINAL_ROUND = 10;

    public final int throwCount;

    public ThrowCount(int throwCount) {
        this.throwCount = throwCount;
    }

    public static ThrowCount of(int throwCount) {
        if (throwCount < MIN_THROW_COUNT || throwCount > MAX_THROW_COUNT) {
            throw new IllegalArgumentException("공던지는 기회는 0 ~ 3번 사이입니다.");
        }
        return new ThrowCount(throwCount);
    }

    public static ThrowCount fisrt() {
        return new ThrowCount(MIN_THROW_COUNT);
    }

    public ThrowCount next(Round round) {
        if (throwCount == 1 && round.round() < FINAL_ROUND) {
            return new ThrowCount(0);
        }
        return new ThrowCount(throwCount + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThrowCount that = (ThrowCount) o;
        return throwCount == that.throwCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(throwCount);
    }

    @Override
    public String toString() {
        return "ThrowCount{" +
                "throwCount=" + throwCount +
                '}';
    }
}
