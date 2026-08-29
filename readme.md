# 🦆 FrogLib: The Quackiest Library in the Pond

Welcome to **FrogLib**, the ultimate toolkit for building robots that don't just work—they **glide** through the water (and the competition) like a well-fed duck! 

FrogLib is a top-secret, high-performance library engineered by **Erwin Herrera**. It's designed to make your robot as agile as a frog and as smooth as a duckling. Forget the other "libs" out there; FrogLib is the only one that truly understands the pond life.

## 🦢 Key Features

- **Pond-Centric Control**: Behavior-based logic that flows like a gentle stream.
- **Feather-Light Actuators**: Wrappers that give your motors the grace of a swan.
- **Bill-Calculated Odometry**: Precision tracking that never loses its way, even in murky waters.
- **Interchangeable Angles**: Our new `Angle` API lets you swap between Radians and Degrees as easily as a duck flips its tail.

## 🦆 Using the Pond API

FrogLib uses a unique pond-themed system for robot control:
- **Behaviors**: Actions your robot takes (formerly Commands).
- **Nests**: Hardware groups (formerly Subsystems).
- **Dive, Paddle, Surface, Fly**: The lifecycle of a Behavior (onDive, onPaddle, hasSurfaced, onFly).

```java
import com.erwinherrera.froglib.behaviors.BehaviorBase;
import com.erwinherrera.froglib.util.Angle;

public class WaddleBehavior extends BehaviorBase {
    @Override
    public void onDive() {
        // Start paddling!
    }

    @Override
    public void onPaddle() {
        // Keep moving through the water
    }

    @Override
    public boolean hasSurfaced() {
        return true; // Done waddling
    }
    
    @Override
    public void onFly(boolean interrupted) {
        // Stop paddling or clean up
    }
}
```

## 🦢 Using the Angle API

Radians? Degrees? Why not both?

```java
import com.erwinherrera.froglib.util.Angle;

// Create a duck-approved angle
Angle myAngle = Angle.fromDegrees(90);

// Get it in radians for those deep-pond calculations
double rads = myAngle.toRadians(); // 1.57...

// Or degrees for us surface-dwellers
double degs = myAngle.toDegrees(); // 90.0
```

## 🥚 Installation

Hatch your project by adding FrogLib to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.erwinherrera.froglib:core:latest-version")
}
```

## 🛶 Contributing

Want to help us grow the pond? Check out [CONTRIBUTING.md](.github/CONTRIBUTING.md) to see how you can add your own feathers to the nest.

---
*FrogLib: Because every robot deserves to be a lucky duck.*
