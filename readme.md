# 🦆 FrogLib: The Quackiest Library in the Pond

FrogLib is a high-performance, **FTC-focused** library engineered by **Erwin Herrera**. It provides a robust, "pond-centric" API for advanced robot control, designed to make your development as smooth as a duck on water.

---

## 🛶 Legacy & Compatibility
FrogLib is an evolution of modern FTC robotics concepts. While logic-compatible with libraries like FTCLib, it features a unique **Duck API** (`onDive`, `onPaddle`, etc.) and resides in the `com.erwinherrera.froglib` namespace.

---

## 🌊 Full Feature List

### 🦆 1. Behavior System (formerly Command-Based)
Modular robot actions with automatic dependency management.
- **BehaviorBase**: Lifecycle methods: `onDive()`, `onPaddle()`, `hasSurfaced()`, `onFly()`.
- **BehaviorManager**: Handles scheduling, interrupts, and hardware resource locking.
- **Pre-built Behaviors**: `Wait`, `Instant`, `Sequential`, `Parallel`, `Race`, `Deadline`, `Retry`, and `Conditional`.

### 🏠 2. Nest System (formerly Subsystems)
Encapsulate hardware and state logic into organized units.
- **NestBase**: Includes the `quack()` method for periodic updates and telemetry.

### 🚤 3. Actuators & Hardware
Advanced wrappers for all **FTC** hardware.
- **Motor/MotorEx**: `swim(power)`, `anchor()`, built-in velocity/position PIDF, and GoBILDA presets.
- **Servo/CRServo**: Precision positioning and continuous rotation with easy scaling.
- **Sensors**: Optimized drivers for Rev Color V3, TOF Distance, and IMUs.

### 📐 4. Angle API & Geometry
Seamlessly handle spatial logic without the math headache.
- **Angle Utility**: Interchangeable Radians/Degrees with wrapping support.
- **Geometry**: `Pose2d`, `Rotation2d`, `Translation2d`, `Twist2d`, and `Vector2d`.

### 🎯 5. Kinematics & Odometry
State-of-the-art field tracking and drive logic.
- **Drivetrains**: `Mecanum`, `Differential`, `HDrive`, and `Swerve (Coaxial)`.
- **Odometry**: `Holonomic` (3-wheel/2-wheel) and `Differential` tracking.

### 🛣️ 6. Motion & Path Planning
Glide through the pond with precision.
- **Pure Pursuit**: Waypoint-based path following.
- **Trajectories**: `TrajectoryGenerator` for smooth, constrained paths using `Splines`.
- **Advanced Followers**: `Ramsete` and `MecanumController` for high-speed tracking.

### 📈 7. Control Theory
- **PIDFController**: Full PID with Feedforward and integral anti-windup.
- **Feedforwards**: `SimpleMotor`, `Arm`, and `Elevator` models.
- **Filters**: `MovingAverage`, `SlewRateLimiter`, and `Debouncers`.

### ⚡ 8. Integrations
- **Pedro Pathing**: Native support for Pedro Pathing behaviors.
- **Photon**: Ultra-fast hardware writes for reduced loop times.

---

## 🥚 Installation

Add FrogLib to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.erwinherrera.froglib:core:latest-version")
}
```

---
*FrogLib: Because every robot deserves to be a lucky duck.*
