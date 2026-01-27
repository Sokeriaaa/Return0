# return 0;

A programming-themed, RTAB-driven RPG game written in Kotlin with Compose Multiplatform.

*Note: This project is under heavy development now; everything could change without notice.*

## Overview

This is a programming-themed, RTAB-driven RPG game built with Kotlin and Compose Multiplatform,
targeting Android, iOS, Web, Desktop (JVM).

Entities (characters) and their functions (skills) are inspired from real classes and functions
in Java, Kotlin, TypeScript, etc.

The project serves as a playground for:

- Action bar-based combat design
- MVI architecture
- Cross-platform UI with Compose Multiplatform
- Data-driven entities and skills
- Fun programming-themed gameplay

## Features

- Action bar based combating
- 12-category balanced effectiveness system (inspired from Pokémon types)
- Characters based on Java/Kotlin classes — each with unique skills
- Unique plugin (weapon) system with entity paths
- Animated damage numbers, glowing, shaking effects in combat
- Emulator for testing parties and enemy setups
- Reusable combat engine in pure Kotlin
- 100% Compose Multiplatform, the only assets used are Material Symbols and Icons from Google Fonts

## Combat Mechanics

### Action Bar

The SPD of entities how quickly they get their next turn. Action bar can also be altered by effects
or functions.

### Category (Type) Effectiveness

A balanced 12-category effectiveness system is implemented in this game. Damage will be multiplied
by 80%~120% based on the categories of function and target entity.

## Architecture

### Summary

This project implements MVI architecture and shared ViewModels, and the UI is in Compose
Multiplatform. Saved data are stored by Room (non-JS platforms) and SQLDelight (JS) via
expect/actual declarations.

### Tech Stack

- Kotlin Multiplatform
- Compose Multiplatform
- MVI architecture
- Koin (DI)
- Room & SQLDelight
- Datastore & Multiplatform Settings

## Build

### Clone

```
git clone --recursive https://github.com/Sokeriaaa/Return0.git
```

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run
widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run
widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run
widget
in your IDE's toolbar or run it directly from the terminal:

- for the Wasm target (faster, modern browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
      ```
- for the JS target (slower, supports older browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:jsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
      ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run
widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

## Road Map

### Multiplatform

| Platform | Supported    |
|----------|--------------|
| Android  | ✔️ Supported |
| iOS      | ❗ Not tested |
| JVM      | ✔️ Supported |
| Web      | ❗ WIP        |

### Game Play

- [x] Game field
- [x] Entities
    - [ ] List
        - [x] Display
        - [ ] Sorting
        - [ ] Filtering
    - [x] Details
        - [x] Profile
        - [x] Functions
        - [x] Plugins
- [x] Teams
    - [x] Display
    - [x] Alter members
    - [x] Create new team
    - [x] Activate team
- [ ] Inventories
    - [ ] Display
    - [ ] Sorting
    - [ ] Filtering
    - [ ] Use item
- [ ] Quests
    - [ ] Display
    - [ ] Sorting
    - [ ] Filtering
    - [ ] Historical quests

### Combat

- [ ] Actions
    - [x] Attack
    - [x] Defend
    - [x] Relax
    - [x] Functions
    - [ ] Items
    - [ ] Escape

---

*The stories in this project are entirely fictional and have no connection whatsoever with any real 
place, person, company, or organization. Any resemblance to actual persons, living or dead, or 
actual events is purely coincidental.*