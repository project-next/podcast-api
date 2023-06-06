# podcast-api

[![Build & JUnits](https://github.com/project-next/podcast-api/actions/workflows/build.yaml/badge.svg)](https://github.com/project-next/podcast-api/actions/workflows/build.yaml)

## Info

An API to serve up RSS metadata for podcasts in database. All info (including the podcast basic info and podcast episodes) will be added to a database the API has access to. There can be multiple podcasts and multiple episodes for each podcast.

RSS data will be compliant with Apple Podcasts and Spotify Podcasts.

[Info on Apple Podcast compliance](https://help.apple.com/itc/podcasts_connect/#/itcb54353390)

## Hibernate L2 Cache

To speed up read queries, an L2 cache is being used when reading from the database. 

In summary, the following need to be done in order to achieve L2 caching:

**Note:** L2 cache is only desirable if there are far more reads than there are writes to a particular DB, else the Cache might be counter-productive.

**Limitations:** L2 cache is a local cache, meaning any updates done to DB from outside the application won't invalidate that field in the cache. If you want to keep cache in sync, minimize writes outside of App instance. You can also evict the cache using
sessionFactory.cache.evictAll()

1. Setup dependencies - `org.hibernate.orm:hibernate-jcache` and `org.hibernate.orm:hibernate-jcache` are needed.
2. Enable L2 cache in [application props file](https://github.com/project-next/podcast-api/blob/750e83591fa72d5c0d04c6bedde60b10bb2af26f/src/main/resources/application.yml#L10-L20)
3. Annotate the POJO/Entity object with @Cacheable

### Links
* https://www.baeldung.com/hibernate-second-level-cache
* https://stackoverflow.com/questions/31585698/spring-boot-jpa2-hibernate-enable-second-level-cache
* [Commit](https://github.com/project-next/podcast-api/commit/750e83591fa72d5c0d04c6bedde60b10bb2af26f)
