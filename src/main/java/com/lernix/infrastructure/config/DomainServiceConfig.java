package com.lernix.infrastructure.config;

import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.domain.service.DeckServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Domain Service Configuration.
 * Explicitly registers domain-logic-heavy services without polluting the domain layer with Spring annotations.
 */
@Configuration
public class DomainServiceConfig {

    /**
     * Registers the DeckService.
     * Requirements: A concrete implementation of DeckRepositoryPort (provided by the Adapter).
     */
    @Bean
    public DeckService deckService(DeckRepositoryPort deckRepositoryPort) {
        return new DeckServiceImpl(deckRepositoryPort);
    }
}

