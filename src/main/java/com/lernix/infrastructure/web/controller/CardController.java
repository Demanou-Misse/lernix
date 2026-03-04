package com.lernix.infrastructure.web.controller;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
import com.lernix.domain.model.DeckId;
import com.lernix.infrastructure.web.dto.request.CreateCardRequest;
import com.lernix.infrastructure.web.dto.request.UpdateCardContentRequest;
import com.lernix.infrastructure.web.dto.response.CardResponse;
import com.lernix.infrastructure.web.mapper.CardMapper;
import com.lernix.shared.response.ApiResponse;
import com.lernix.application.usecase.card.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Flashcard Management.
 * Follows the 2026 Enterprise API Standards.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Card Management", description = "Endpoints for creating and managing flashcards within decks")
public class CardController {

    private final CreateCardUseCase createCardUseCase;
    private final UpdateCardContentUseCase updateCardContentUseCase;
    private final GetDeckCardsUseCase getDeckCardsUseCase;
    private final GetCardDetailsUseCase getCardDetailsUseCase;
    private final DeleteCardUseCase deleteCardUseCase;
    private final CardMapper cardMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new card", description = "Adds a flashcard to a specific deck")
    public ApiResponse<CardResponse> create(@RequestBody @Valid CreateCardRequest request) {
        log.info("REST request to add card to deck: {}", request.deckId());

        Card card = createCardUseCase.execute(
                new DeckId(UUID.fromString(request.deckId())),
                request.front(),
                request.back()
        );

        return ApiResponse.success(cardMapper.toResponse(card), "Card created successfully");
    }

    @GetMapping("/deck/{deckId}")
    @Operation(summary = "List cards in a deck", description = "Retrieves all cards belonging to the specified deck")
    public ApiResponse<List<CardResponse>> getByDeck(@PathVariable UUID deckId) {
        log.debug("Fetching all cards for deck: {}", deckId);
        List<Card> cards = getDeckCardsUseCase.execute(new DeckId(deckId));
        return ApiResponse.success(cardMapper.toResponseList(cards), "Cards retrieved");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get card details", description = "Fetch a single card by its unique ID")
    public ApiResponse<CardResponse> getDetails(@PathVariable UUID id) {
        Card card = getCardDetailsUseCase.execute(new CardId(id));
        return ApiResponse.success(cardMapper.toResponse(card), "Card details retrieved");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update card content", description = "Modify the front or back side of an existing card")
    public ApiResponse<CardResponse> update(@PathVariable UUID id, @RequestBody @Valid UpdateCardContentRequest request) {
        log.info("Updating content for card: {}", id);
        Card updated = updateCardContentUseCase.execute(new CardId(id), request.front(), request.back());
        return ApiResponse.success(cardMapper.toResponse(updated), "Card content updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete card", description = "Permanently remove a card")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        log.warn("REST request to delete card: {}", id);
        deleteCardUseCase.execute(new CardId(id));
        return ApiResponse.success(null, "Card successfully removed from inventory");
    }

}

