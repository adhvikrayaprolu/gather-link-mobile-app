# Summary
Review legacy Firebase data and approve a safe ownership-rules rollout.

## Problem
Main historically stores Users.password and Groups.ownerEmail; new rules use ownerUid and normalized membership IDs. Deploying them blindly could block old groups.

## Why this matters
Accurate remaining work prevents duplicated implementation and keeps the portfolio safe and reproducible.

## Current behavior
Emulator code/rules pass in https://github.com/adhvikrayaprolu/gather-link-mobile-app/pull/6. `firebase login:list` reports no authorized accounts; no live records or rules were inspected or changed.

## Desired behavior
A verified migration/no-migration decision and explicit owner-approved rollout preserve correct ownership without retaining plaintext credentials.

## Proposed implementation
1. Owner authorizes read-only access to `gatherlink-9b50d`; confirm environment/project ID against app config. Current CLI has no authorized accounts, so existence/extent of legacy live data is unknown.
2. Inventory counts and schema only (never log passwords/tokens/emails). Inspect legacy Users password-field presence, Groups missing ownerUid, and GroupMemberships IDs. Back up Firestore and current rules to access-controlled owner storage; review retention/security because exports may contain legacy secrets.
3. Derive group ownership from legacy `ownerEmail` using authoritative Firebase Auth records. Never guess a UID: missing/ambiguous accounts are blockers for owner review. Confirm each groupId matches its document ID and every post's userId/groupId is valid.
4. Stage a sanitized representative copy in demo-gatherlink. Transform membership IDs to `{userId}_{groupId}`; deduplicate by this pair preserving earliest joinedAt, report missing users/groups. Set ownerUid and ensure owner membership consistently. Remove Users.password in the proposed target; do not retain plaintext login fallback.
5. Dry-run a resumable/idempotent migration with counts, invariants, collision/quarantine report and no sensitive values. Repeat dry-run and emulator tests; demonstrate no-op second run and rollback/recovery procedure. Decide backup retention and credential incident response with the owner.
6. Obtain separate explicit approval for production migration AND restrictive rule deployment. Plan a maintenance window/client compatibility and migration ordering before enforcing ownerUid rules; otherwise existing groups may become inaccessible.
7. Only after owner approval, execute reviewed batches, verify totals and ownership, then deploy reviewed rules and smoke-test approved accounts. No production action is authorized by this issue's existence. Capture sanitized evidence; stop on any mismatch.

## Relevant files / modules
app/google-services.json, firestore.rules, ProfileData, GroupModel, GroupMembershipModel, docs/firebase-migration-review.md

## Acceptance criteria
- [ ] Owner confirms project/access and read-only inventory
- [ ] Legacy field/identity/collision counts recorded without sensitive values
- [ ] Sanitized emulator migration dry-run and second-run idempotence pass
- [ ] Rollback, backup retention and unresolved identities reviewed
- [ ] Owner explicitly approves any production writes/rules deployment
- [ ] Approved rollout verified or no-migration conclusion documented

## Testing requirements
Offline migration fixtures: missing/ambiguous ownerEmail, duplicate membership IDs, invalid group references, plaintext password field removal, no-op rerun and rollback. Existing owner/outsider rules and native SDK tests remain green.

## Validation commands
`./gradlew check assembleDebug`; `npm ci`; `npm run test:rules`; `npm run emulators`; `./gradlew -PfirebaseEmulators=true connectedDebugAndroidTest`

## Dependencies
#1 implementation in https://github.com/adhvikrayaprolu/gather-link-mobile-app/pull/6; authenticated owner-approved live read-only access and explicit later production approval.

## Agent execution notes
Human-review only. Never login to another project, print sensitive fields, rewrite live data or deploy rules automatically. Read-only review and emulators first.

## Definition of done
Owner accepts the verified live-data assessment and separately approved rollout/no-migration outcome; all evidence is sanitized.

Tracking issue: https://github.com/adhvikrayaprolu/gather-link-mobile-app/issues/7
