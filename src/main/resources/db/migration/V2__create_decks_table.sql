-- Schema for Deck Management
CREATE TABLE decks (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    title VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_deck_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_decks_owner_id ON decks(owner_id);
