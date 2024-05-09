CREATE TABLE podcast_info
	(
		podcast_id CHAR(36) NOT NULL
		, title VARCHAR(50) NOT NULL
		, link VARCHAR(255) NOT NULL
		, description VARCHAR(3000) NOT NULL
		, language VARCHAR(5) NOT NULL
		, copyright VARCHAR(40) NOT NULL
		, last_build_date TIMESTAMP NOT NULL
		, email VARCHAR(30) NOT NULL
		, category VARCHAR(20) NOT NULL
		, author VARCHAR(30) NOT NULL
		, is_explicit BOOLEAN NOT NULL
		, image_url VARCHAR(255) NOT NULL
		, PRIMARY KEY(podcast_id)
	);

CREATE TABLE podcast_episode
	(
		episode_id SERIAL
		, podcast_id CHAR(36) NOT NULL
		, title VARCHAR(100) NOT NULL
		, webpage_link VARCHAR(255) NOT NULL
		, audio_link VARCHAR(255) NOT NULL
		, description VARCHAR(3000) NOT NULL
		, pub_date TIMESTAMP NOT NULL
		, author VARCHAR(30) NOT NULL
		, image VARCHAR(255) NOT NULL
		, keywords VARCHAR(300) NOT NULL
		, guid CHAR(36) NOT NULL
		, length INT NOT NULL
		, media_type VARCHAR(15) NOT NULL
		, is_explicit BOOLEAN NOT NULL
		, duration TIME NOT NULL
        ,season int2 NOT NULL
		, PRIMARY KEY(title, podcast_id, season)
		, FOREIGN KEY(podcast_id) REFERENCES podcast_info(podcast_id)
	);