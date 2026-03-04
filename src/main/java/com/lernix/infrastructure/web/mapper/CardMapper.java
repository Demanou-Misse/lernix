package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.model.Card;
import com.lernix.infrastructure.web.dto.response.CardResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardMapper {

    public CardResponse toResponse(Card domain) {
        return new CardResponse(
                domain.id().value(),
                domain.deckId().value(),
                domain.content().front(),
                domain.content().back(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }

    public List<CardResponse> toResponseList(List<Card> domains) {
        return domains.stream()
                .map(this::toResponse)
                .toList(); // Java 21 optimized stream
    }
}

