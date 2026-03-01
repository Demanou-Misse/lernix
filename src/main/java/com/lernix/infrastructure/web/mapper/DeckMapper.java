package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.model.Deck;
import com.lernix.infrastructure.web.dto.response.DeckResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeckMapper {

    public DeckResponse toResponse(Deck domain) {
        return new DeckResponse(
                domain.id().value(),
                domain.ownerId().value(),
                domain.title().value(),
                domain.description(),
                domain.status().name(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }

    public List<DeckResponse> toResponseList(List<Deck> domains) {
        return domains.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

