package com.lernix.domain.service;

import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.model.UserId;

public interface DeckService {
    // Checks if a deck title is already taken by the same user.
    boolean isTitleUniqueForUser(UserId ownerId, DeckTitle title);
}

