package com.lernix.domain.algorithm;

import com.lernix.domain.enums.CardState;
import com.lernix.domain.model.ReviewMetaData;

/**
 * Immutable DTO representing the output of an SRS calculation.
 */
public record ReviewResult(
        CardState nextState,
        ReviewMetaData nextMetaData
) {}

