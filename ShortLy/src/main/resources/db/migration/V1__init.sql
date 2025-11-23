CREATE TABLE links (
id SERIAL PRIMARY KEY,
short_key VARCHAR(20) UNIQUE NOT NULL,
original_url TEXT NOT NULL,
title TEXT,
is_public BOOLEAN DEFAULT TRUE,
click_count BIGINT DEFAULT 0,
created_at TIMESTAMP DEFAULT now()
);


CREATE TABLE clicks (
id SERIAL PRIMARY KEY,
link_id INTEGER REFERENCES links(id) ON DELETE CASCADE,
clicked_at TIMESTAMP DEFAULT now(),
referer TEXT,
user_agent TEXT,
ip_address TEXT
);