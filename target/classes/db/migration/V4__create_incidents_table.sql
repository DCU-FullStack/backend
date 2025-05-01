CREATE TABLE incidents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    camera_id BIGINT,
    detection_type VARCHAR(50) NOT NULL,
    confidence FLOAT NOT NULL,
    location VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (camera_id) REFERENCES cameras(id)
); 