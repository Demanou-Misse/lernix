package com.lernix.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagTest {

    @Test
    void shouldNormalizeTagValue() {
        Tag tag = new Tag("  Medicine  ");
        assertThat(tag.value()).isEqualTo("medicine");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "tag with spaces", "invalid@symbol", ""})
    void shouldRejectInvalidTags(String input) {
        assertThatThrownBy(() -> new Tag(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
