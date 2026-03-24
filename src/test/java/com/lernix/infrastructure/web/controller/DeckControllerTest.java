package com.lernix.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lernix.application.usecase.deck.*;
import com.lernix.domain.model.*;
import com.lernix.infrastructure.web.dto.request.CreateDeckRequest;
import com.lernix.infrastructure.web.mapper.DeckMapper;
import jakarta.persistence.EntityManager; // ADDED
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeckController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DeckMapper.class)
@DisplayName("Web: Deck Controller Functional Tests")
class DeckControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // FIX 1: Mock the persistence infrastructure that DeckMapper is begging for
    @MockitoBean private jakarta.persistence.EntityManager entityManager;
    @MockitoBean private jakarta.persistence.EntityManagerFactory entityManagerFactory;

    // FIX 2: If your DeckMapper also uses JpaTagRepository, mock it too!
    @MockitoBean private com.lernix.infrastructure.persistence.repository.JpaTagRepository tagRepository;

    // --- All your Use Case Mocks ---
    @MockitoBean private CreateDeckUseCase createDeckUseCase;
    @MockitoBean private FindUserDecksUseCase findUserDecksUseCase;
    @MockitoBean private UpdateDeckUseCase updateDeckUseCase;
    @MockitoBean private ArchiveDeckUseCase archiveDeckUseCase;
    @MockitoBean private DeleteDeckUseCase deleteDeckUseCase;
    @MockitoBean private GetDeckDetailsUseCase getDeckDetailsUseCase;

    @Test
    @DisplayName("POST /api/v1/decks - Should return 201")
    void shouldCreateDeckViaApi() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateDeckRequest request = new CreateDeckRequest(userId.toString(), "Vocabulary", "Desc");

        // Ensure Domain Deck reflects your rich class structure
        Deck mockDeck = Deck.create(new UserId(userId), new DeckTitle("Vocabulary"), "Desc");

        when(createDeckUseCase.execute(any(), any(), any())).thenReturn(mockDeck);

        mockMvc.perform(post("/api/v1/decks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
