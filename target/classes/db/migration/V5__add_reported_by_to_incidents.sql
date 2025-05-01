ALTER TABLE incidents
ADD COLUMN reported_by BIGINT,
ADD CONSTRAINT fk_incidents_reported_by
FOREIGN KEY (reported_by) REFERENCES users(id); 