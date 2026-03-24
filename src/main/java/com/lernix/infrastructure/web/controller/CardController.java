package com.lernix.infrastructure.web.controller;

import com.lernix.domain.model.*;
import com.lernix.infrastructure.web.dto.request.CardReviewRequest;
import com.lernix.infrastructure.web.dto.request.CreateCardRequest;
import com.lernix.infrastructure.web.dto.response.CardResponse;
import com.lernix.infrastructure.web.mapper.CardMapper;
import com.lernix.shared.response.ApiResponse;
import com.lernix.application.usecase.card.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Card Management", description = "Endpoints for SRS reviews and tag-based study sessions")
public class CardController {

    private final CreateCardUseCase createCardUseCase;
    private final UpdateCardContentUseCase updateCardContentUseCase;
    private final GetDeckCardsUseCase getDeckCardsUseCase;
    private final GetCardDetailsUseCase getCardDetailsUseCase;
    private final DeleteCardUseCase deleteCardUseCase;
    private final ProcessCardReviewUseCase processCardReviewUseCase;
    private final GetCardsByTagUseCase getCardsByTagUseCase;

    private final CardMapper cardMapper;

    @PostMapping
    @Operation(summary = "Create a new card", description = "Adds a flashcard to a specific deck with initial metadata.")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CardResponse> create(@RequestBody @Valid CreateCardRequest request) {
        log.info("REST request to add card to deck: {}", request.deckId());
        Card card = createCardUseCase.execute(
                new DeckId(UUID.fromString(request.deckId())),
                request.front(),
                request.back(), request.tags()
        );
        return ApiResponse.success(cardMapper.toResponse(card), "Card created successfully");
    }

    /**
     * CORE FEATURE: Submit a review for a card.
     * Uses the SM-2 / FSRS algorithm logic.
     */
    @PatchMapping("/{id}/review")
    @Operation(summary = "Submit card review", description = "Apply SRS algorithm based on user feedback (AGAIN, HARD, GOOD, EASY)")
    public ApiResponse<CardResponse> review(
            @PathVariable UUID id,
            @RequestBody @Valid CardReviewRequest request) {

        log.info("Processing review for card {} with grade {}", id, request.grade());
        Card card = processCardReviewUseCase.execute(new CardId(id), request.grade());
        return ApiResponse.success(cardMapper.toResponse(card), "Review processed successfully");
    }

    /**
     * MASTER FEATURE: Study by Tag.
     * Allows filtering cards by a specific tag and status (due or all).
     */
    @GetMapping("/tags/{tagName}")
    @Operation(summary = "Get cards by tag", description = "Filter cards by tag name. Use 'onlyDue=true' for active study sessions.")
    public ApiResponse<List<CardResponse>> getByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "false") boolean onlyDue,
            @Parameter(hidden = true) @AuthenticationPrincipal UserId currentUserId) {

        log.debug("Fetching cards for tag: {} (onlyDue: {})", tagName, onlyDue);
        // userId should come from JWT security context in a real 2026 app
        List<Card> cards = getCardsByTagUseCase.execute(currentUserId, tagName, onlyDue);

        return ApiResponse.success(cardMapper.toResponseList(cards), "Filtered cards retrieved");
    }

    @GetMapping("/deck/{deckId}")
    @Operation(summary = "List cards in deck", description = "Retrieves all cards associated with a specific deck ID.")
    public ApiResponse<List<CardResponse>> getByDeck(@PathVariable UUID deckId) {
        List<Card> cards = getDeckCardsUseCase.execute(new DeckId(deckId));
        return ApiResponse.success(cardMapper.toResponseList(cards), "Cards retrieved");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get card details", description = "Retrieves full content and SRS metadata for a single card.")
    public ApiResponse<CardResponse> getDetails(@PathVariable UUID id) {
        Card card = getCardDetailsUseCase.execute(new CardId(id));
        return ApiResponse.success(cardMapper.toResponse(card), "Card details retrieved");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete card", description = "Permanently remove a card")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        log.warn("REST request to delete card: {}", id);
        deleteCardUseCase.execute(new CardId(id));

        return ApiResponse.success(null, "Card successfully removed from inventory");
    }

}


