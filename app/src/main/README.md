## Navigation Overview

```text
app/src/main/
│
├── java/com/example/gatherlink/ → Java classes (Activities, Fragments, Models, Adapters, Utils)
├── res/ → UI Resources (XML layouts, styles, drawables, etc.)
└── AndroidManifest.xml → Component declarations & app-wide permissions
```

---

## AndroidManifest.xml

This file is the **blueprint of the app**. It declares:

- All app components (activities, fragments, services, etc.)
- Required permissions (internet, storage access, etc.)
- Firebase configurations
- The app's launcher activity
- Themes and intent filters

Located at:
```text
app/src/main/AndroidManifest.xml
```

## UI Resources – res/ Folder

The res/ directory holds all frontend design and UI-related files, including:
- layout/ – XML layout files used in Activities and Fragments
- drawable/ – App icons, background images, vector assets
- values/ – Strings, colors, styles, themes, and dimension constants
- mipmap/ – Launcher icons in various resolutions
- menu/ – Optional menu definitions

These files follow Android’s resource qualifier system and are crucial for responsive and visually consistent UI across devices.

## Java Source Folder

To view all the logic for screens, adapters, models, and helper classes, navigate to:
```text
java/com/example/gatherlink/
```

That folder includes all the backend functionality for your app — and has its own README that breaks down the subfolders and purpose of each file.
