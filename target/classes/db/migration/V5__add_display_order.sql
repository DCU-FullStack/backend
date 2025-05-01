ALTER TABLE incidents ADD COLUMN display_order INTEGER NOT NULL DEFAULT 0;
UPDATE incidents SET display_order = id; 