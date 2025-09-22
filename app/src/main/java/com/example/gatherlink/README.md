# Application Logic – `java/com/example/gatherlink/`

This directory contains all the **Java source files** responsible for the functionality, interaction logic, and Firebase integration for the GatherLink app. Each subfolder is organized by type (activities, adapters, models, utilities, etc).

---

## Folder Structure & Descriptions

### `activity/` — User-Facing Screens

This folder contains **all screen-based activities** in the app, representing major UI components and user interactions.

**Key Files:**

- `MainActivity.java` – Launcher screen for the app.
- `LoginActivity.java`, `LoginPage.java`, `SignUpActivity.java` – User authentication and onboarding.
- `HomeActivity.java` – Home screen post-login, manages navigation and fragments.
- `ExploreGroupsActivity.java` – Displays a list of available groups using `GroupAdapter`.
- `CreateGroupActivity.java` – UI and logic for creating a new group.
- `GroupDetailsActivity.java` – Details of a group including members, posts, etc.
- `GroupPostsActivity.java` – Displays all posts from a selected group.
- `PostActivity.java` – Used to create a new post in a group.
- `PostDetailsActivity.java` – Detailed view of an individual post.
- `MyGroupsActivity.java` – Shows the groups the current user is part of.
- `MyPostsActivity.java` – Displays all posts authored by the current user.

Each activity uses `findViewById`, `RecyclerView`, and Firebase Firestore to handle real-time interaction and storage.


---

### `adapters/` — UI Binders for RecyclerViews

This folder includes **custom adapters** that connect Java objects (like groups and posts) to UI elements.

**Key Files:**

- `GroupAdapter.java` – Binds group data to the group list view; supports two modes: "Explore" and "MyGroups".
- `PostAdapter.java` – Binds post data to the feed view inside each group.

Adapters use `ViewHolder` patterns and handle click listeners, data loading, and Firestore updates.

---

### `fragment/` — Modular UI Components

Reusable UI components managed inside activities via `FragmentManager`.

**Key Files:**

- `HomeFragment.java` – Displays summary/dashboard or recent activity after login.
- `ProfileFragment.java` – Manages user profile and logout options.

Fragments are swapped into the `HomeActivity` to allow a tab-based experience.

---

### `model/` — Firebase-Compatible Data Models

Defines **POJO (Plain Old Java Objects)** used to serialize and deserialize data to/from Firebase Firestore.

**Key Files:**

- `GroupModel.java` – Represents a group with fields like `groupId`, `groupName`, `description`, `ownerEmail`, and `createdAt`.
- `GroupMembershipModel.java` – Maps users to groups with `userId`, `groupId`, and `joinedAt`.
- `PostsModel.java` – Structure for posts inside a group including `message`, `userId`, `timestamp`, etc.

Each model has getters/setters and a no-argument constructor for Firestore.


---

### `utils/` — Helper Functions

Includes reusable helper logic for tasks like checking user group membership, formatting dates, etc.

**Key File:**

- `GroupMembershipUtil.java` – Checks if a user is part of a given group using a Firestore query callback (`isUserInGroup()`).

---

## Navigation Paths

- **Screens (UI logic):** `activity/`
- **Backend display logic (UI binders):** `adapters/`
- **App-wide models:** `model/`
- **Helpers and static utility logic:** `utils/`

---

## Firebase Integration

This directory includes all the backend logic that communicates with Firebase:

- Firestore for dynamic data (`Groups`, `GroupMemberships`, `Posts`)
- FirebaseAuth for user identity
- Firestore listeners for real-time updates

Every interaction is designed to be asynchronous and uses `.addOnSuccessListener()` / `.addOnFailureListener()` blocks.
