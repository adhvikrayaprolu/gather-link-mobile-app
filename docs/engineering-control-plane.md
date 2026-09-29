# Engineering control plane

Join interest groups and create/interact with posts on Android.

## Setup and validation
Use JDK17, Android SDK35 and an ignored local.properties sdk.dir if needed. ./gradlew check assembleDebug builds/lints; published main contains no meaningful test sources. Firebase client config is not an admin secret; do not deploy rules or mutate cloud data automatically.

## Verified state
Published main audit SHA: `56146194a0393cbfb453cbf37d9ce34d37d19a21`. No root AGENTS.md, issues or PRs existed at this audit. No existing Actions pipeline or meaningful behavior test suite in published main.

## Unmerged work
Earlier local branch `codex/gatherlink-mobile-security-foundation` at `667ae047d2bd4946f13120cc380fed765662e574` has tested improvements, but is not hosted or merged. Review/reuse it before reimplementing. Its reported checks are not checks of this control-plane branch.

## Backlog and stop rule
Use GitHub issues after publication; local draft identifiers must never be treated as GitHub issue numbers. Portfolio tracking covers core flows, safe configuration, meaningful tests, green PR CI, reproducible setup and concise demo documentation. Stop after the tracker is complete; no speculative features.

## Queue
`is:issue is:open label:"automation:ready" sort:updated-asc` scoped to this repository. Apply priority and dependency checks from AGENTS.md.
