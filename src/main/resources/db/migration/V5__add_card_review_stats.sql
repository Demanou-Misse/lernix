-- V5: Add Spaced Repetition Statistics to Cards table
-- Standard SQL compliant: Split ALTER TABLE for H2/PostgreSQL compatibility

-- 1. Lifecycle state (NEW, LEARNING, REVIEW, RELEARNING)
ALTER TABLE cards ADD COLUMN state VARCHAR(20) NOT NULL DEFAULT 'NEW';

-- 2. Ease Factor (EF) - Standard SM2 starting point is 2.5
ALTER TABLE cards ADD COLUMN ease_factor DOUBLE PRECISION NOT NULL DEFAULT 2.5;

-- 3. Interval and Repetitions
ALTER TABLE cards ADD COLUMN review_interval INTEGER NOT NULL DEFAULT 0;
ALTER TABLE cards ADD COLUMN repetitions INTEGER NOT NULL DEFAULT 0;

-- 4. Next Review Date (UTC)
ALTER TABLE cards ADD COLUMN next_review_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 5. Performance Indexing
-- Essential for querying due cards: SELECT * FROM cards WHERE next_review_date <= NOW()
CREATE INDEX idx_cards_next_review ON cards(next_review_date);
CREATE INDEX idx_cards_state ON cards(state);

-- 6. Schema Documentation
COMMENT ON COLUMN cards.state IS 'Current SRS state of the flashcard';
COMMENT ON COLUMN cards.ease_factor IS 'The multiplier for interval growth (Ease Factor)';
COMMENT ON COLUMN cards.review_interval IS 'The current gap in days before the next review';
