package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain: ReviewMetaData Value Object Tests")
class ReviewMetaDataTest {

    @Test
    @DisplayName("Should create initial metadata with system defaults (v2.0 Blueprint)")
    void shouldCreateInitialMetadata() {
        ReviewMetaData metadata = ReviewMetaData.createInitial();

        assertThat(metadata.easeFactor()).isEqualTo(2.5);
        assertThat(metadata.interval()).isZero();
        assertThat(metadata.repetitions()).isZero();
        assertThat(metadata.nextReview()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should maintain immutability and valid data")
    void shouldConstructValidMetadata() {
        Instant future = Instant.now().plusSeconds(86400);
        ReviewMetaData metadata = new ReviewMetaData(2.65, 4, 1, future);

        assertThat(metadata.easeFactor()).isEqualTo(2.65);
        assertThat(metadata.interval()).isEqualTo(4);
        assertThat(metadata.nextReview()).isEqualTo(future);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 1.29, 0.0, -1.0})
    @DisplayName("Should throw exception if Ease Factor is below 1.3 (Ease Hell Prevention)")
    void shouldProtectAgainstEaseHell(double invalidEF) {
        assertThatThrownBy(() -> new ReviewMetaData(invalidEF, 1, 1, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ease Factor cannot be below the 1.3 floor");
    }

    @Test
    @DisplayName("Should throw exception for negative intervals or repetitions")
    void shouldRejectNegativeValues() {
        assertThatThrownBy(() -> new ReviewMetaData(2.5, -1, 0, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new ReviewMetaData(2.5, 0, -1, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should reject null nextReview date")
    void shouldRejectNullDate() {
        assertThatThrownBy(() -> new ReviewMetaData(2.5, 0, 0, null))
                .isInstanceOf(NullPointerException.class);
    }
}

