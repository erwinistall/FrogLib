# Math and Geometry

Accurate movement requires precise math. FrogLib provides a suite of geometry classes to help you navigate the pond.

## The Angle API

The `Angle` class is a central utility for handling rotations. It allows you to work seamlessly with both Degrees and Radians without worrying about conversions.

```java
Angle a = Angle.fromDegrees(90);
double rad = a.toRadians(); // 1.57...
Angle b = Angle.fromRadians(Math.PI);
Angle c = a.plus(b);
```

Key features:
- `wrap()`: Normalizes the angle to `[-180, 180]` degrees.
- `wrapPositive()`: Normalizes the angle to `[0, 360]` degrees.

## Geometry Classes

### Pose2d
A `Pose2d` represents the robot's position (`x`, `y`) and its heading (rotation). It is the standard way to describe where the robot is on the field.

### Translation2d and Rotation2d
- `Translation2d`: Represents a 2D vector (x, y).
- `Rotation2d`: Represents a rotation or heading.

These classes support standard vector operations like `plus`, `minus`, `times`, and `rotateBy`.

```java
Pose2d currentPose = new Pose2d(10, 10, Rotation2d.fromDegrees(45));
Transform2d movement = new Transform2d(new Translation2d(5, 0), new Rotation2d(0));
Pose2d newPose = currentPose.plus(movement);
```
