# Motion and Path Following

Once you know where your robot is, you need to move it smoothly to where you want it to be. FrogLib provides several high-level tools for motion control.

## Path Following

A `Path` is a series of `Waypoints` that define a route for the robot to follow.

### Pure Pursuit
The `PurePursuitBehavior` allows your robot to follow a path by constantly "looking ahead" to a target point on the path. This results in smooth, organic movement that handles curves beautifully.

```java
Path path = new Path()
    .addWaypoint(new StartWaypoint(0, 0))
    .addWaypoint(new GeneralWaypoint(24, 24))
    .addWaypoint(new EndWaypoint(48, 0));

Behavior followPath = new PurePursuitBehavior(drivebase, path);
followPath.schedule();
```

## Trajectories

For even more precise control, `Trajectory` objects combine paths with velocity and acceleration constraints.

### Ramsete Controller
The `RamseteBehavior` uses a nonlinear controller to track a trajectory. It is particularly effective for Differential (tank) drive robots, ensuring they stay perfectly on the path even at high speeds.

## Motion Profiling

FrogLib includes support for **Trapezoidal Motion Profiles**, which ensure your robot starts and stops smoothly by limiting acceleration and velocity.

```java
TrapezoidProfile profile = new TrapezoidProfile(
    new Constraints(maxVel, maxAccel),
    new State(goal, 0)
);
```
