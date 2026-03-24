package com.lernix.domain.algorithm;

import com.lernix.domain.enums.CardState;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.model.ReviewMetaData;
import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class SM2Engine implements SpacedRepetitionEngine {

    private static final double EF_MIN = 1.3;
    private static final double JITTER_FACTOR = 0.05;

    @Override
    public ReviewResult calculateNextReview(CardState currentState, ReviewMetaData current, ReviewGrade grade) {
        Instant now = Instant.now();

        return switch (currentState) {
            case NEW -> handleNewCard(grade, now);
            case LEARNING -> handleLearning(current, grade, now);
            case REVIEW -> handleReview(current, grade, now);
            case RELEARNING -> handleRelearning(current, grade, now);
        };
    }

    private ReviewResult handleNewCard(ReviewGrade grade, Instant now) {
        if (grade == ReviewGrade.EASY) {
            return new ReviewResult(CardState.REVIEW, new ReviewMetaData(2.65, 4, 1, schedule(now, 4)));
        }
        // Note 0, 1, 3 go to step 1
        return new ReviewResult(CardState.LEARNING, new ReviewMetaData(2.5, 1, 0, schedule(now, 1)));
    }

    private ReviewResult handleLearning(ReviewMetaData current, ReviewGrade grade, Instant now) {
        return switch (grade) {
            case EASY -> new ReviewResult(CardState.REVIEW, new ReviewMetaData(2.65, 4, 1, schedule(now, 4)));
            case GOOD -> {
                if (current.interval() < 3) {
                    yield new ReviewResult(CardState.LEARNING, new ReviewMetaData(2.5, 3, 0, schedule(now, 3)));
                }
                yield new ReviewResult(CardState.REVIEW, new ReviewMetaData(2.5, 4, 1, schedule(now, 4)));
            }
            // Covers AGAIN and HARD: restart Step 1
            default -> new ReviewResult(CardState.LEARNING, new ReviewMetaData(2.5, 1, 0, schedule(now, 1)));
        };
    }

    private ReviewResult handleReview(ReviewMetaData current, ReviewGrade grade, Instant now) {
        if (grade == ReviewGrade.AGAIN) {
            return new ReviewResult(CardState.RELEARNING, new ReviewMetaData(calculateEF(current.easeFactor(), 0), 1, 0, schedule(now, 1)));
        }

        double newEF = calculateEF(current.easeFactor(), grade.getValue());
        double multiplier = (grade == ReviewGrade.EASY) ? newEF * 1.2 : newEF;

        int nextInterval = (grade == ReviewGrade.HARD) ? (int) Math.round(current.interval() * 1.2) : (int) Math.round(current.interval() * multiplier);
        nextInterval = applyJitter(nextInterval);

        return new ReviewResult(CardState.REVIEW, new ReviewMetaData(newEF, nextInterval, current.repetitions() + 1, schedule(now, nextInterval)));
    }

    private ReviewResult handleRelearning(ReviewMetaData current, ReviewGrade grade, Instant now) {
        if (grade.getValue() >= 3) {
            int recoveryInterval = Math.max(4, (int) Math.round(current.interval() * 0.2));
            return new ReviewResult(CardState.REVIEW, new ReviewMetaData(current.easeFactor(), recoveryInterval, 1, schedule(now, recoveryInterval)));
        }
        return new ReviewResult(CardState.RELEARNING, new ReviewMetaData(current.easeFactor(), 1, 0, schedule(now, 1)));
    }

    private double calculateEF(double oldEF, int gradeValue) {
        double adjustment = switch (gradeValue) {
            case 5 -> 0.15;
            case 3 -> 0.0;
            case 1 -> -0.20;
            case 0 -> -0.50;
            default -> 0.0;
        };
        return Math.max(EF_MIN, oldEF + adjustment);
    }

    private int applyJitter(int interval) {
        if (interval < 4) return interval;
        double randomFactor = ThreadLocalRandom.current().nextDouble(1 - JITTER_FACTOR, 1 + JITTER_FACTOR);
        return (int) Math.max(1, Math.round(interval * randomFactor));
    }

    private Instant schedule(Instant from, int days) {
        return from.plus(Duration.ofDays(days));
    }
}
