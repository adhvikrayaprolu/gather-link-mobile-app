# GatherLink – Mobile App

GatherLink is an Android app designed to connect users through interest-based groups. Users can create, join, and interact within groups by posting messages and engaging with others in real-time.

Built using Android Studio with Firebase for authentication and real-time data storage, GatherLink offers a streamlined and responsive experience built entirely in Java.

---

## Features
- Create and manage groups
- Post and interact with messages within groups
- Register, log in, and maintain secure user sessions
- Firebase Authentication and Firestore backend
- Firebase integration for authentication and Firestore for real-time data
- Clean, intuitive UI using Material Design

---

## Tech Stack

- **Java (Android SDK)** – Application logic and UI behavior
- **XML** – Frontend UI layouts and styling
- **Firebase Suite:**
  - Firebase Authentication
  - Firebase Firestore (NoSQL Database)
  - Firebase Storage (for media or post attachments)
- **Gradle** – Dependency and build management
- **Android Studio** – IDE for development and testing

---

## Directory Structure (Simplified)

```text
GatherLink/
│
├── app/
│   ├── src/
│   │   ├── main/                                → App logic and UI
│   │   │   ├── java/com/example/gatherlink/     → Core Java classes: activities, fragments, adapters, models, utils
│   │   │   ├── AndroidManifest.xml              → App component declarations, permissions, and intent filters
│   │   │   └── res/                             → UI layout XMLs, drawables, values, themes
│   ├── build.gradle                             → App-level Gradle configuration
│   └── proguard-rules.pro                       → Proguard settings
├── .gitignore
├── build.gradle                   → Project-level Gradle config
└── README.md                      → Main project documentation
```

## Navigation Notes

### To dive into core app functionality, navigate to:
```text
app/src/main/java/com/example/gatherlink/
```

### To explore UI and resources, go to:
```text
app/src/main/res/
```

## Manifest file:
Declares:
- Application metadata (app name, icon, themes)
- All app Activity and Fragment components
- Firebase permissions and integrations
- Launch activity and deep linking capabilities

## Other Notes

- google-services.json is required for Firebase to function properly.
- If you're cloning this project, make sure to sync Gradle and enable Firebase with your own config.
- Sensitive credentials (like Firebase keys) should be rotated if this repository becomes public.
