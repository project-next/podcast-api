---
name: releasing-the-api
description: Use when cutting a release for podcast-api — choosing the version, creating and pushing a version tag, or publishing GitHub release notes.
---

# Releasing the API

## Overview

podcast-api is a single-project Kotlin / Spring Boot 4 service built with Gradle and shipped as a
Spring Boot fat JAR. It lives at `project-next/podcast-api`; the default branch is `release`. Tags
are bare `vX.Y.Z`.

**There is exactly one version, in `build.gradle.kts`:**

```kotlin
version = "1.8.7"
```

It flows automatically: the `BootJar` task writes it into the JAR manifest as
`Implementation-Version`, `Constants.APP_VERSION` reads it back with
`Constants::class.java.getPackage().implementationVersion ?: "LOCAL"`, and `StatusController`
serves it from `GET /api/v1/status`. Nothing else needs editing.

**The `LOCAL` trap:** `implementationVersion` is null unless the code is running *from a built JAR*.
Under `./gradlew bootRun` or in the IDE, `/status` reports `LOCAL` — that is expected, not a bug,
and it means the release version can only be confirmed by running the actual built JAR.

## Pre-flight

Run from an up-to-date checkout of `release`.

```bash
grep -n '^version' build.gradle.kts
PREV=$(git tag --list 'v*' --sort=-v:refname | head -1)
git log --oneline "$PREV"..HEAD
git diff --stat "$PREV"..HEAD
./gradlew build
```

**`build.gradle.kts` is the source of truth — read it first.** If it already names an unreleased
version (says `1.8.7` while the newest tag is `v1.8.6`), that is the version to cut. Match it and
skip the bump table.

**If it is stale** — still naming the version `$PREV` already released — **stop. Do not tag.**
Report which version it should become and let the user commit and push that bump. A tag whose JAR
manifest carries the previous version makes `/status` report the wrong release, and nothing on the
server will contradict it (see Common mistakes).

`./gradlew build` must pass. It is exactly what `.github/workflows/build.yaml` runs, and that
workflow triggers on `tags: v**` — so anything failing here becomes a permanent red check on an
already-published tag. Note `build` also runs the jacoco floors from `gradle/unitTest.gradle.kts`
(LINE ≥ 75%, BRANCH ≥ 50%); a coverage regression fails the build even when every test passes.

## Choosing the version

Only needed when the version hasn't already been decided. This repo has never cut a major and has
been on `v1` since the start.

| Bump | When |
| --- | --- |
| Patch | Dependency updates, bug fixes, internal refactors, routine runtime/toolchain bumps — no change to any response shape. Most releases here are this |
| Minor | A new capability or endpoint (`v1.7.0` added podcast/episode deletion; `v1.2.0` added create/modify), or a framework migration that changes how the service is deployed (`v1.8.0`, Spring Boot 4 + UBI image) |
| Major | A breaking change to the JSON contract or the RSS feed. Avoid — see below |

**The RSS feed is the strictest part of the contract.** `GET /podcast/{id}` and
`/podcast/feed/{id}` serve XML consumed by Apple Podcasts and Spotify, which poll it directly and
are not clients you can coordinate an upgrade with. Treat a change to the feed's shape as breaking
even when the JSON endpoints are untouched.

## Release notes

**Title:** `vX.Y.Z`, or `vX.Y.Z: Short Theme` when the release has a headline —
`v1.8.0: Spring Boot 4 Migration + UBI Image Usage`. Bare tag name is right for routine releases.

**Body** is GitHub's generated notes with hand-written bullets inserted at the top, under the
heading:

```
## What's Changed
* <hand-written bullet describing a user-visible change>
* <another>

* Update jetty monorepo to v12.1.12 by @renovate[bot] in <PR url>
* Update Gradle to v9.7.0 by @renovate[bot] in <PR url>

**Full Changelog**: https://github.com/project-next/podcast-api/compare/<PREV>...<NEW>
```

Renovate automerges minor/patch here, so most releases genuinely are dependency roll-ups — the
generated PR list is the content, not noise. Keep it. The hand-written bullets go **above** it,
separated by a blank line, and cover only what a human did.

Seed the file from GitHub rather than typing the PR list by hand:

```bash
gh api repos/project-next/podcast-api/releases/generate-notes \
  -f tag_name="vX.Y.Z" -f previous_tag_name="$PREV" --jq .body > notes.md
```

Then edit `notes.md` to insert the hand-written bullets under `## What's Changed`. Write it to a
scratch directory, not into the repo.

## Sequence

Show the version, the diff, the `./gradlew build` result, and the drafted notes. Get approval
**once**. Then run the rest without stopping again:

```bash
git tag vX.Y.Z <commit>          # lightweight: no -a, no -m
git push origin vX.Y.Z
gh api repos/project-next/podcast-api/releases/generate-notes \
  -f tag_name="vX.Y.Z" -f previous_tag_name="$PREV" --jq .body > notes.md
# insert hand-written bullets under "## What's Changed"
gh release create vX.Y.Z --repo project-next/podcast-api \
  --title "vX.Y.Z" --notes-file notes.md
```

Push the tag first. `gh release create` attaches to an existing tag but invents one from the default
branch when the tag is missing, and `generate-notes` needs the tag to exist to compute the range.

## Why approval comes before the push

The pushed tag is what production is built from, and the GitHub Release is what podcast consumers
see. Approval is the last cheap moment — after the push, a wrong version is corrected with another
release, not an edit.

## Common mistakes

- **`git tag -a`.** Every tag here is lightweight (`git cat-file -t v1.8.6` → `commit`). An
  annotated tag carries a message nobody reads; the notes belong in the GitHub Release.
- **Editing a version anywhere but `build.gradle.kts`.** There is no second copy. `Constants.kt`
  reads the JAR manifest — hardcoding a version there would break the one mechanism that keeps
  `/status` honest.
- **Trusting `/status` from a local run.** It says `LOCAL` unless the process was started from a
  built JAR. Only a real JAR run proves the version.
- **Assuming the deployed JAR's filename tells you the version.** `createDockerJar` renames
  `podcast-api-1.8.7.jar` to `podcast-api.jar` so Docker can mount a stable path — a stale deploy
  looks identical on disk and only `/status` reveals it.
- **Running `./gradlew test` instead of `./gradlew build`.** `test` skips the jacoco coverage
  verification that CI enforces, so a coverage regression surfaces as a red check on a tag that is
  already public.
- **Retyping the renovate PR list.** Use the `generate-notes` API; hand-copying it is how entries
  get dropped or point at the wrong PR.
- **Dropping the Full Changelog footer.** `v1.8.0` and `v1.8.2` are missing it. It is the last line
  of every other release.
