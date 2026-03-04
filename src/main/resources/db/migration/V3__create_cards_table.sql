-- Schema for Card Management
CREATE TABLE cards (
    id UUID PRIMARY KEY,
    deck_id UUID NOT NULL,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    -- Referential Integrity: If a deck is deleted, its cards are wiped too
    CONSTRAINT fk_cards_deck FOREIGN KEY (deck_id) REFERENCES decks(id) ON DELETE CASCADE
);

-- Optimization: Index on Foreign Key for fast retrieval by deck
CREATE INDEX idx_cards_deck_id ON cards(deck_id);
