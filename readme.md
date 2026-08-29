# 🦆 FrogLib: The Quackiest Library in the Pond

Welcome to **FrogLib**, the ultimate toolkit for building robots that don't just work—they **glide** through the water (and the competition) like a well-fed duck! 

FrogLib is a high-performance, "pond-centric" library engineered by **Erwin Herrera**. It packs all the power of legacy libraries but with a much slicker, duck-approved API.

---

## 🛶 Does it have the same features as SolversLib?
**Yes!** FrogLib is a complete evolution of SolversLib. Every feature you know and love (PID, Odometry, Pure Pursuit, etc.) is here, but renamed to fit the pond theme. It's the same robust engine under a much more aerodynamic featherset.

## 🦢 Is it compatible with FTCLib?
**Logic-wise, yes. Code-wise, no.** FrogLib has moved to the `com.erwinherrera.froglib` namespace and uses a unique "Duck API" (`onDive`, `onPaddle`, etc.). To migrate from FTCLib, you'll need to update your imports and method names, but the underlying concepts are identical.

---

## 🌊 Feature Deep Dive (The Grand Tour)

### 1. 🦆 The Behavior System (formerly Command-Based)
Organize your robot's actions into modular **Behaviors**. The **BehaviorManager** (your Pond Master) handles scheduling and requirements automatically.

```java
public class WaddleBehavior extends BehaviorBase {
    private final DriveNest nest;

    public WaddleBehavior(DriveNest nest) {
        this.nest = nest;
        addRequirements(nest);
    }

    @Override
    public void onDive() { /* Logic when the duck enters the water */ }

    @Override
    public void onPaddle() { /* Logic while the duck is swimming */ }

    @Override
    public boolean hasSurfaced() { return false; /* Is the mission done? */ }

    @Override
    public void onFly(boolean interrupted) { /* Cleanup logic */ }
}
```

### 2. 🏠 Nests (formerly Subsystems)
Encapsulate your hardware into **Nests**. Use the **quack()** method for periodic updates (telemetry, state logic, etc.).

```java
public class IntakeNest extends NestBase {
    private final Motor intakeMotor;

    public void quack() {
        // This runs every loop cycle!
        telemetry.addData("Intake Power", intakeMotor.get());
    }
}
```

### 3. 🚤 Actuators (formerly Hardware)
Our **Motor** and **Servo** wrappers are feather-light and powerful.
- **swim(power)**: Set power with automatic caching and scaling.
- **anchor()**: Stop the motor instantly.

```java
Motor lift = new Motor(hardwareMap, "lift", Motor.GoBILDA.RPM_435);
lift.swim(0.5); // Start swimming
lift.anchor();  // Stop and drop anchor
```

### 4. 📐 The Angle API & Geometry
Radians? Degrees? Why choose? The **Angle** class makes math simple.
- **Pose2d**, **Rotation2d**, and **Translation2d** handle all your field positioning.

```java
Angle target = Angle.fromDegrees(90);
double rads = target.toRadians(); // 1.57...

Rotation2d rotation = new Rotation2d(target);
Pose2d robotPose = new Pose2d(10, 10, rotation);
```

### 5. 🎯 Odometry & Tracking
Keep track of your robot's position in the pond with **Holonomic** or **Differential** odometry.

```java
// Setup for a 3-wheel duck tracker
Odometry duckTracker = new HolonomicOdometry(
    leftEncoder::getPosition,
    rightEncoder::getPosition,
    backEncoder::getPosition,
    TRACK_WIDTH, CENTER_WHEEL_OFFSET
);
```

### 6. 🎮 Enhanced Gamepads
**GamepadEx** provides button debouncing, toggle support, and easy trigger reading.

```java
GamepadEx duckDriver = new GamepadEx(gamepad1);
if (duckDriver.wasJustPressed(GamepadKeys.Button.A)) {
    // Single quack detected!
}
```

### 6. 🛣️ Path Following & Trajectories
- **Pure Pursuit**: Smoothly follow a path of waypoints.
- **Trajectory Generator**: Create complex, curvaceous paths with velocity constraints.
- **Ramsete & Mecanum Controllers**: High-level follower logic for advanced drivetrains.

### 7. 📈 Precision Control
- **PIDFController**: Proportional, Integral, Derivative, and Feedforward.
- **SimpleMotorFeedforward**: Static, Velocity, and Acceleration gains (ks, kv, ka).
- **SlewRateLimiter**: Prevents jerky movements and motor burnout.

---

## 🥚 Installation

Hatch your project by adding FrogLib to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.erwinherrera.froglib:core:latest-version")
}
```

## 🛶 Contributing

Want to add more feathers to the nest? Check out [CONTRIBUTING.md](.github/CONTRIBUTING.md).

---
*FrogLib: Because every robot deserves to be a lucky duck.*
