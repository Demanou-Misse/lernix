package com.lernix.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lernix.application.usecase.card.*;
import com.lernix.domain.model.*;
import com.lernix.infrastructure.web.dto.request.CreateCardRequest;
import com.lernix.infrastructure.web.mapper.CardMapper;
import com.lernix.usecase.card.*;
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

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CardMapper.class)
@DisplayName("Web: CardController Unit Tests")
class CardControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CreateCardUseCase createCardUseCase;
    @MockitoBean private UpdateCardContentUseCase updateCardContentUseCase;
    @MockitoBean private GetDeckCardsUseCase getDeckCardsUseCase;
    @MockitoBean private GetCardDetailsUseCase getCardDetailsUseCase;
    @MockitoBean private DeleteCardUseCase deleteCardUseCase;

    @Test
    @DisplayName("POST /api/v1/cards - Should return 201 when card is created")
    void shouldCreateCardViaApi() throws Exception {
        UUID deckId = UUID.randomUUID();
        CreateCardRequest request = new CreateCardRequest(deckId.toString(), "Front text", "Back text");
        Card mockCard = Card.create(new DeckId(deckId), "Front text", "Back text");

        when(createCardUseCase.execute(any(), any(), any())).thenReturn(mockCard);

        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.front").value("Front text"));
    }
}

