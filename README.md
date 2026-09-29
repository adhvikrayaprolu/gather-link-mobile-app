# GatherLink Android

Discover interest groups, join communities and publish group posts through a native Java/XML Android app backed by Firebase.

## What it does / key features

Email/password Firebase Authentication, profile names, group discovery/creation/membership and group posts. This is a group-post product: the unused empty ChatActivity was removed; private chat and media attachments are not implemented.

## Screenshots / demo

Use a disposable Firebase project or emulator: sign up → create a group → join as a second user → create/view/edit/delete that user's post → update profile → log out. Runtime navigation screenshots remain a verification task; no fabricated screenshots.

## Architecture / tech stack

Android API26+ / target35, Java11 language level, Gradle8.11.1 + Android plugin8.10.1, Firebase Auth and Firestore. Activities/fragments handle screen state, adapters bind lists, POJO models persist groups/posts; ProfileData constructs metadata without credentials. Firebase Auth is the sole credential store.

## Quick start

Install JDK17, Android SDK35 and Android Studio (or command-line SDK). Set ANDROID_HOME or local.properties `sdk.dir` to your SDK. Replace `app/google-services.json` with your own Android Firebase client configuration for `com.example.gatherlink`, enable email/password Auth and Firestore.

```sh
./gradlew check assembleDebug
```

This is the canonical validation/build command. Install `app/build/outputs/apk/debug/app-debug.apk` on an emulator/device for the demo. CI validates unit tests, lint and debug assembly.

## Configuration / security

`google-services.json` is public client configuration, not a service-account private key. It does not establish Firestore access control. Review API key restrictions, enable Auth and review rules in your own Firebase project. Never commit Admin SDK credentials.

`firestore.rules` is staged for review; no rules are automatically deployed. Rules bind profiles to UID, allow only profile metadata, protect group ownership, require membership for creating posts and preserve author/group on mutations. Internal activities are nonexported; launcher is the only exported activity.

**Existing data needs human review:** earlier signup copied plaintext passwords to `Users`. Stop using any deployed old app, remove those fields with an authorized admin migration and consider resetting affected credentials after review. This PR does not delete live data or rotate credentials. Existing Groups need verified `ownerUid`; existing random membership IDs must migrate to `<uid>_<groupId>` before these rules are deployed. Test on a disposable project first. Updates to legacy profiles still containing password are denied until cleaned.

## Testing

```sh
./gradlew check assembleDebug
npm ci
npm run test:rules
```

Rules tests run only `demo-gatherlink` locally using the Firestore emulator at 127.0.0.1:8089, require JDK17, and exercise owner/member denial and password rejection. No production credential is required. Local unit tests verify the persisted profile schema.

## Project structure / data model

`app/src/main/java/com/example/gatherlink/{activity,fragment,adapters,model,utils}` and `res/` contain native application code/layouts. `Users/{uid}` stores names/email/timestamps only; `Groups/{id}` stores `ownerUid`, identity/description; `GroupMemberships/{uid}_{groupId}` stores membership; `Groups/{id}/Posts/{id}` stores author/text/likes.

## Design decisions / known limitations

Firebase rules are the authorization boundary; client checks are only feedback. No Docker is needed for Android. Likes remain simple counters without per-user deduplication. Some list/auth/payload failure paths need more instrumentation coverage. No live Firebase deployment or runtime authorization proof is claimed from a successful build. Android SDK/Gradle artifacts require downloads on first run.

## Future work

Finish the audited navigation/empty/error-state tests and capture real emulator screenshots. Add measured query improvements only if network counts justify them.

## Isolated Android emulator workflow
No live Firebase writes are needed for a local demo. With an Android emulator running:

```sh
npm ci
npm run emulators
# In a second terminal:
./gradlew -PfirebaseEmulators=true installDebug
./gradlew -PfirebaseEmulators=true connectedDebugAndroidTest
```

The opt-in debug build switches Auth and Firestore to `demo-gatherlink` on localhost (Android host bridge `10.0.2.2`, ports 9099/8089). Release always disables this flag. Debug permits cleartext only for emulator development; release retains platform network protections. Emulator data is disposable and is never exported to the live project. Native SDK tests cover registration, credential-free profiles, group creation, membership, post editing, cross-user denial and sign-in. The standard build does not run emulator-dependent instrumentation tests.
