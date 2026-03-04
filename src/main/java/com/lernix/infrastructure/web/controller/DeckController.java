package com.lernix.infrastructure.web.controller;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.UserId;
import com.lernix.infrastructure.web.dto.request.CreateDeckRequest;
import com.lernix.infrastructure.web.dto.request.UpdateDeckRequest;
import com.lernix.infrastructure.web.dto.response.DeckResponse;
import com.lernix.infrastructure.web.mapper.DeckMapper;
import com.lernix.shared.response.ApiResponse;
import com.lernix.application.usecase.deck.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/decks")
@RequiredArgsConstructor
@Tag(name = "Deck Management", description = "Endpoints for creating, managing and organizing card decks")
public class DeckController {

    private final CreateDeckUseCase createDeckUseCase;
    private final UpdateDeckUseCase updateDeckUseCase;
    private final ArchiveDeckUseCase archiveDeckUseCase;
    private final DeleteDeckUseCase deleteDeckUseCase;
    private final FindUserDecksUseCase findUserDecksUseCase;
    private final GetDeckDetailsUseCase getDeckDetailsUseCase;
    private final DeckMapper deckMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new deck", description = "Registers a new deck for a specific user")
    public ApiResponse<DeckResponse> create(@RequestBody @Valid CreateDeckRequest request) {
        log.info("REST request to create deck: {} for user: {}", request.title(), request.ownerId());

        Deck deck = createDeckUseCase.execute(
                new UserId(UUID.fromString(request.ownerId())),
                request.title(),
                request.description()
        );

        return ApiResponse.success(deckMapper.toResponse(deck), "Deck created successfully");
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List user decks", description = "Retrieves all decks owned by the specified user")
    public ApiResponse<List<DeckResponse>> getUserDecks(@PathVariable UUID userId) {
        log.debug("Fetching all decks for user: {}", userId);
        List<Deck> decks = findUserDecksUseCase.execute(new UserId(userId));
        return ApiResponse.success(deckMapper.toResponseList(decks), "User decks retrieved");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get deck details", description = "Fetch a single deck by its unique ID")
    public ApiResponse<DeckResponse> getDetails(@PathVariable UUID id) {
        Deck deck = getDeckDetailsUseCase.execute(new DeckId(id));
        return ApiResponse.success(deckMapper.toResponse(deck), "Deck details retrieved");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update deck", description = "Partially update deck title or description")
    public ApiResponse<DeckResponse> update(@PathVariable UUID id, @RequestBody @Valid UpdateDeckRequest request) {
        log.info("Updating deck: {}", id);
        Deck updated = updateDeckUseCase.execute(new DeckId(id), request.title(), request.description());
        return ApiResponse.success(deckMapper.toResponse(updated), "Deck updated successfully");
    }

    @PatchMapping("/{id}/archive")
    @Operation(summary = "Archive deck", description = "Set deck status to ARCHIVED")
    public ApiResponse<DeckResponse> archive(@PathVariable UUID id) {
        log.warn("Archiving deck: {}", id);
        Deck archived = archiveDeckUseCase.execute(new DeckId(id));
        return ApiResponse.success(deckMapper.toResponse(archived), "Deck archived");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete deck", description = "Permanently remove a deck from the system")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        log.warn("REST request to delete deck: {}", id);
        deleteDeckUseCase.execute(new DeckId(id));
        return ApiResponse.success(null, "Deck and all its cards successfully deleted");
    }
}

