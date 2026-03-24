-- V6: Enterprise Tagging System
-- Strategy: Many-to-Many with Normalized Tag Dictionary

-- 1. Create Tag Dictionary (Ensures one record per unique tag name)
CREATE TABLE tags (
    id UUID PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL, -- Normalized: lowercase, no spaces (validated by Domain)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Create Join Table (Mapping between Cards and Tags)
CREATE TABLE card_tags (
    card_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (card_id, tag_id), -- Prevents duplicate tag-card associations
    CONSTRAINT fk_card_tags_card FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE,
    CONSTRAINT fk_card_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- 3. High-Performance Indexing
-- Essential for "Get Cards By Tag" Use Case performance
CREATE INDEX idx_card_tags_tag_id ON card_tags(tag_id);
CREATE INDEX idx_tags_name ON tags(name);

-- 4. Audit Trail (Senior requirement for traceability)
COMMENT ON TABLE tags IS 'Global dictionary of unique study tags';
COMMENT ON TABLE card_tags IS 'Many-to-many relationship mapping cards to their respective tags';

