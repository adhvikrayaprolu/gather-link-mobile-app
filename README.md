# GatherLink Android
A native Android client for discovering interest groups, joining communities and publishing group posts.

## Overview
GatherLink brings interest-based communities to Android using Firebase Authentication and Firestore.

## Project Context
Developed during a software engineering internship at ClayHR. The [web application](https://github.com/adhvikrayaprolu/gather-link-web-app) explores the same product through Spring MVC/JSP. The clients have separate data stores; there is no shared web/mobile account or database synchronization.

## Key Features
- Email/password Firebase Authentication and credential-free profile metadata.
- Group creation, discovery, membership and member posting.
- Post edit/delete by the author, guarded navigation and useful empty/error states.
- Reviewable owner/member Firestore rules and local emulator regression tests.

## Architecture / Tech Stack
Android Java/XML + Material components → Firebase Auth/Firestore. Activities/fragments handle screens, adapters bind lists, and `ProfileData` creates metadata without passwords. Java11 language level, JDK17 tooling, SDK35, minimum Android26.

## Quick Start
Install JDK17, Android SDK35 and Node22. Set `ANDROID_HOME` to the SDK (macOS commonly `$HOME/Library/Android/sdk`), or configure ignored `local.properties` with `sdk.dir`.
```sh
./gradlew check assembleDebug
npm ci
npm run emulators
```
Keep the local Auth/Firestore emulators running. In another terminal:
```sh
./gradlew -PfirebaseEmulators=true installDebug
```
Open GatherLink on an Android emulator. The opt-in debug build points to `demo-gatherlink` at `10.0.2.2`; it never needs a live Firebase login. Normal/release builds do not use this emulator configuration.

## Validation / Tests
```sh
./gradlew check assembleDebug
npm run test:rules
```
With the Android device and Firebase emulators running, also execute:
```sh
./gradlew -PfirebaseEmulators=true connectedDebugAndroidTest
```
Unit, Firestore rules and native SDK tests verify metadata, account/group/post operations and unauthorized access. CI builds/lints/runs unit and rules tests; connected-device testing is local to keep CI reliable.

## Environment / Security
`app/google-services.json` identifies `gatherlink-9b50d`; it is client configuration, not an Admin SDK secret. Live project access was not authorized in the current Firebase CLI session. Use your own client configuration for live testing. Never commit service-account keys.

Existing deployments may contain plaintext `Users.password` fields, groups without `ownerUid`, or random membership IDs. Do not deploy restrictive rules until an owner approves the migration plan in GitHub Issues. No production data or rules were changed by this pass.

## Project Structure
`app/src/main/java/`: activities, fragments, adapters and models; `app/src/main/res/`: XML UI; `app/src/test/`: unit tests; `app/src/androidTest/`: native emulator tests; `firestore.rules` and `tests/`: rules and regression suite.

## Current Status / Limitations
Private chat and media attachments are not implemented; the unused empty ChatActivity was removed. Live Firebase migration/deployment and visual review are separate from emulator data-flow validation.

## Related Projects
[GatherLink Web](https://github.com/adhvikrayaprolu/gather-link-web-app) — browser client with Spring/JPA persistence. Read [AGENTS.md](AGENTS.md) before agent work.
