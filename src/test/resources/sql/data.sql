INSERT INTO podcast
	(
		podcast_id
		, title, link
		, description
		, language, copyright, last_build_date, email
		, category, author, is_explicit
		, image_url
	)
VALUES
	(
		'41c4e54d-bee9-43c9-b34b-d4eb87c1a377'
		, 'Random Podcast Name', 'https://www.thesupremekingscastle.com'
		, 'The BEST test podcast in the world!'
		, 'en-us', 'Test 2022', NOW(), 'whoami@gmail.com'
		, 'Comedy', 'NEXT', true
		, 'https://www.thesupremekingscastle.com'
	);


INSERT INTO podcast_episode
(
	title, podcast_id
	, webpage_link
	, audio_link
	, description
	, pub_date, author
	, image
	, keywords
	, episode_id, length, media_type, is_explicit
	, duration
) VALUES
	(
		'#1', '41c4e54d-bee9-43c9-b34b-d4eb87c1a377'
		, 'https://www.thesupremekingscastle.com'
		, 'https://www.thesupremekingscastle.com'
		, 'The first episode!'
		, '2020-06-15 17:36:00', 'NEXT'
		, 'https://www.thesupremekingscastle.com'
		, 'Tag1|Tag2'
		, '8380ee02-cd3f-4c96-85bd-238a0cd2ab90', 327610467, 'audio/x-m4a', true
		, '02:01:37'
	);