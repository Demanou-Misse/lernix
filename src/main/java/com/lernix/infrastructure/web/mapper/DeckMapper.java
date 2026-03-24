package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.enums.DeckStatus;
import com.lernix.domain.model.*;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import com.lernix.infrastructure.web.dto.response.DeckResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeckMapper {

    @PersistenceContext
    private EntityManager entityManager;

    public Deck toDomain(DeckEntity entity) {
        if (entity == null) return null;

        return new Deck(
                new DeckId(entity.getId()),
                new UserId(entity.getOwner().getId()),
                new DeckTitle(entity.getTitle()),
                entity.getDescription(),
                DeckStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public DeckEntity toEntity(Deck domain) {
        // Use a Proxy for the UserEntity because we only need the ID for the Foreign Key
        UserEntity ownerProxy = entityManager.getReference(UserEntity.class, domain.ownerId().value());

        return DeckEntity.builder()
                .id(domain.id().value())
                .title(domain.title().value())
                .description(domain.description())
                .status(domain.status().name())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .owner(ownerProxy)
                .build();
    }

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

