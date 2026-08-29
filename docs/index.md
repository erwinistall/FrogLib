# Welcome to the Pond 🐸

Welcome to **FrogLib**, the premier behavior-based library for FTC robots. FrogLib is designed to help your robot move through the water (and the field) with grace, efficiency, and a touch of amphibian charm.

## What is FrogLib?

FrogLib is a complete rewrite and refactor of modern FTC libraries, centered around a declarative **Behavior-based** architecture. Instead of managing complex state machines manually, you define what your robot should *do* (its Behaviors) and where it lives (its Nests).

## Why FrogLib?

- **Declarative Logic**: Compose complex robot actions using simple building blocks.
- **Hardware Abstraction**: Actuators that "swim" and "anchor" seamlessly.
- **Robust Math**: A powerful Angle API and Geometry classes (Pose2d, Translation2d) to keep your robot on course.
- **Advanced Motion**: Built-in support for Pure Pursuit, Ramsete, and Trajectory following.

## Installation

Add the following to your `build.gradle` to start your journey in the pond:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.erwinherrera:FrogLib:v1.0.0'
}
```

Jump in and start quacking!
