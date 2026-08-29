# Nests (Subsystems)

A **Nest** is where your hardware lives. It encapsulates motors, servos, and sensors into a logical unit, such as an Intake, a Lift, or a Drivetrain.

## Creating a Nest

To create a Nest, implement the `Nest` interface. The most important method is `quack()`, which is called periodically by the `BehaviorManager`.

```java
public class IntakeNest implements Nest {
    private final Motor intakeMotor;

    public IntakeNest(HardwareMap hwMap) {
        intakeMotor = new Motor(hwMap, "intake");
        register(); // Register with the BehaviorManager
    }

    public void setPower(double power) {
        intakeMotor.swim(power);
    }

    @Override
    public void quack() {
        // Periodic updates (e.g., telemetry, safety checks)
    }
}
```

## Requirements and Mutual Exclusion

When a `Behavior` is scheduled, it declares which Nests it requires. The `BehaviorManager` ensures that only one behavior can use a Nest at a time. If a new behavior starts that requires a Nest already in use, the old behavior is "kicked out" (interrupted) to make room for the new one.

## Default Behaviors

You can set a default behavior for a Nest. If no other behavior is using the Nest, the default behavior will automatically start. This is perfect for things like keeping a lift at a specific height or keeping an intake idle.

```java
intakeNest.setDefaultBehavior(new IntakeIdleBehavior(intakeNest));
```
