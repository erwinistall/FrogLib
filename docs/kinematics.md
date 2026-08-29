# Kinematics and Odometry

Understanding how your robot's wheel movements translate to field-relative motion is the job of **Kinematics**.

## Drivebases

FrogLib supports various drivebase configurations out of the box:

- **MecanumDrive**: For holonomic movement in any direction.
- **DifferentialDrive**: For standard tank or arcade drive robots.
- **SwerveDrive**: For advanced coaxial swerve modules.

## Odometry

Odometry is the process of estimating the robot's position over time by tracking wheel encoders or dead-wheel encoders.

### Holonomic Odometry
Used for Mecanum or Swerve drives. It tracks `x`, `y`, and `heading`.

### Differential Odometry
Used for Tank drives. It tracks the distance traveled by both sides and the change in heading.

```java
// Example setup for Holonomic Odometry
Odometry odo = new HolonomicOdometry(
    leftEncoder, 
    rightEncoder, 
    perpEncoder, 
    trackWidth, 
    centerOffset
);

// In your loop
odo.update();
Pose2d currentPos = odo.getPose();
```
