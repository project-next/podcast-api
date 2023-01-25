INSERT INTO podcast_info
	(
		podcast_id
		, podcast_title, podcast_link
		, podcast_description
		, podcast_language, podcast_copyright, podcast_last_build_date, podcast_email
		, podcast_category, podcast_author, is_explicit
		, podcast_image_url
	)
VALUES
	(
		'41c4e54d-bee9-43c9-b34b-d4eb87c1a377'
		, 'Random Podcast Name', 'https://www.thesupremekingscastle.com'
		, 'The BEST test podcast in the workd!'
		, 'en-us', 'Test 2022', NOW(), 'whoami@gmail.com'
		, 'Comedy', 'NEXT', true
		, 'https://www.thesupremekingscastle.com'
	);


INSERT INTO podcast_episode
(
	episode_title, podcast_id
	, episode_audio_link
	, episode_description
	, episode_pub_date, episode_author
	, episode_image
	, episode_keywords
	, episode_guid, episode_length, episode_media_type, is_episode_explicit
	, episode_duration
) VALUES
	(
		'#1', '41c4e54d-bee9-43c9-b34b-d4eb87c1a377'
		, 'https://www.thesupremekingscastle.com'
		, 'The first episode!'
		, '2020-06-15 17:36:00', 'NEXT'
		, 'https://www.thesupremekingscastle.com'
		, 'Tag1|Tag2'
		, '8380ee02-cd3f-4c96-85bd-238a0cd2ab90', 327610467, 'audio/x-m4a', true
		, '02:01:37'
	);