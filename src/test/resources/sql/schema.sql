CREATE TABLE podcast_info
	(
		podcast_id CHAR(36) NOT NULL
		, podcast_title VARCHAR(50) NOT NULL
		, podcast_link VARCHAR(255) NOT NULL
		, podcast_description VARCHAR(3000) NOT NULL
		, podcast_language VARCHAR(5) NOT NULL
		, podcast_copyright VARCHAR(40) NOT NULL
		, podcast_last_build_date TIMESTAMP NOT NULL
		, podcast_email VARCHAR(30) NOT NULL
		, podcast_category VARCHAR(20) NOT NULL
		, podcast_author VARCHAR(30) NOT NULL
		, is_explicit BOOLEAN NOT NULL
		, podcast_image_url VARCHAR(255) NOT NULL
		, PRIMARY KEY(podcast_id)
	);

CREATE TABLE podcast_episode
	(
		episode_id SERIAL
		, podcast_id CHAR(36) NOT NULL
		, episode_title VARCHAR(100) NOT NULL
		, episode_audio_link VARCHAR(255) NOT NULL
		, episode_description VARCHAR(3000) NOT NULL
		, episode_pub_date TIMESTAMP NOT NULL
		, episode_author VARCHAR(30) NOT NULL
		, episode_image VARCHAR(255) NOT NULL
		, episode_keywords VARCHAR(300) NOT NULL
		, episode_guid CHAR(36) NOT NULL
		, episode_length INT NOT NULL
		, episode_media_type VARCHAR(15) NOT NULL
		, is_episode_explicit BOOLEAN NOT NULL
		, episode_duration TIME NOT NULL
		, PRIMARY KEY(episode_title)
		, FOREIGN KEY(podcast_id) REFERENCES podcast_info(podcast_id)
	);