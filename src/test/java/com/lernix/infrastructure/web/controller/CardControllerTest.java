package com.lernix.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lernix.application.usecase.card.*;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.Tag;
import com.lernix.infrastructure.web.dto.request.CardReviewRequest;
import com.lernix.infrastructure.web.dto.request.CreateCardRequest;
import com.lernix.infrastructure.web.dto.request.UpdateCardContentRequest;
import com.lernix.infrastructure.web.dto.response.CardResponse;
import com.lernix.infrastructure.web.mapper.CardMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // Required for 2026 Security Standards
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // Required for CSRF simulation
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@DisplayName("Web: Card Controller Functional Tests")
@WithMockUser(username = "senior.dev@lernix.io") // FIX: Simulates an authenticated user for all tests
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Modern Spring Boot 3.4+ Mocking Strategy ---
    @MockitoBean private CreateCardUseCase createCardUseCase;
    @MockitoBean private UpdateCardContentUseCase updateCardContentUseCase;
    @MockitoBean private GetDeckCardsUseCase getDeckCardsUseCase;
    @MockitoBean private GetCardDetailsUseCase getCardDetailsUseCase;
    @MockitoBean private DeleteCardUseCase deleteCardUseCase;
    @MockitoBean private ProcessCardReviewUseCase processCardReviewUseCase;
    @MockitoBean private GetCardsByTagUseCase getCardsByTagUseCase;
    @MockitoBean private CardMapper cardMapper;

    @Test
    @DisplayName("POST /api/v1/cards -> Should create a card and return 201")
    void shouldCreateCard() throws Exception {
        UUID deckId = UUID.randomUUID();
        CreateCardRequest request = new CreateCardRequest(deckId.toString(), "Front", "Back", Set.of("tag1"));

        Card mockCard = Card.create(new DeckId(deckId), "Front", "Back", Set.of(new Tag("tag1")));
        when(createCardUseCase.execute(any(), any(), any(), any())).thenReturn(mockCard);

        // Simuler le mapping du retour
        when(cardMapper.toResponse(any())).thenReturn(new CardResponse(UUID.randomUUID(), deckId, "Front", "Back", "NEW", Instant.now(), Set.of("tag1"), Instant.now(), Instant.now()));

        mockMvc.perform(post("/api/v1/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.front").value("Front")); // Vérifier que le JSON contient les données
    }


    @Test
    @DisplayName("PATCH /api/v1/cards/{id}/review -> Should process SRS review")
    void shouldProcessReview() throws Exception {
        UUID cardId = UUID.randomUUID();
        CardReviewRequest request = new CardReviewRequest(ReviewGrade.GOOD);

        // Arrange: We need to mock a return card because we changed the UseCase to return a Card
        Card mockCard = Card.create(new DeckId(UUID.randomUUID()), "Q", "A", Set.of());
        when(processCardReviewUseCase.execute(any(), any())).thenReturn(mockCard);

        // Mocking the mapper response to ensure JSON path $.data exists if needed
        when(cardMapper.toResponse(any())).thenReturn(new CardResponse(cardId, UUID.randomUUID(), "Q", "A", "REVIEW", Instant.now(), Set.of(), Instant.now(), Instant.now()));

        mockMvc.perform(patch("/api/v1/cards/{id}/review", cardId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Review processed successfully"));

        verify(processCardReviewUseCase).execute(eq(new CardId(cardId)), eq(ReviewGrade.GOOD));
    }


    @Test
    @DisplayName("GET /api/v1/cards/tags/{tagName} -> Should filter cards by tag")
    void shouldGetCardsByTag() throws Exception {
        String tagName = "anatomy";

        mockMvc.perform(get("/api/v1/cards/tags/{tagName}", tagName)
                        .param("onlyDue", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(getCardsByTagUseCase).execute(any(), eq(tagName), eq(true));
    }

    @Test
    @DisplayName("PATCH /api/v1/cards/{id}/review -> Should return 400 when grade is invalid")
    void shouldFailForInvalidGrade() throws Exception {
        UUID cardId = UUID.randomUUID();
        String invalidPayload = "{\"grade\": \"AWESOME\"}";

        mockMvc.perform(patch("/api/v1/cards/{id}/review", cardId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/v1/cards/{id} -> Should remove card and return 200")
    void shouldDeleteCard() throws Exception {
        UUID cardId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/cards/{id}", cardId)
                        .with(csrf())) // FIX: Pass CSRF check for DELETE
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Card successfully removed from inventory"));

        verify(deleteCardUseCase).execute(new CardId(cardId));
    }
}
