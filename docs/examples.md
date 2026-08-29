# Curated Examples

Here are some common robot tasks implemented using FrogLib's behavior-based architecture.

## 1. Simple TeleOp Drive

```java
public class TeleOpDrive extends PondOpMode {
    private MecanumDrive drive;
    private GamepadEx gamepad;

    @Override
    public void onInit() {
        drive = new MecanumDrive(hardwareMap, "frontLeft", "frontRight", "backLeft", "backRight");
        gamepad = new GamepadEx(gamepad1);
        
        // Set default behavior for the drivebase
        drive.setDefaultBehavior(new RunBehavior(() -> {
            drive.swim(
                gamepad.getLeftY(),
                gamepad.getLeftX(),
                gamepad.getRightX()
            );
        }, drive));
    }
}
```

## 2. Sequential Autonomous Actions

```java
// Drive to a position, wait, then move an intake
Behavior autoRoutine = new SequentialBehaviorGroup(
    new DriveToPoseBehavior(drive, new Pose2d(24, 24, 0)),
    new WaitBehavior(500),
    new InstantBehavior(() -> intake.swim(1.0)).withTimeout(1000),
    new InstantBehavior(() -> intake.anchor())
);
```

## 3. Parallel Tasks

```java
// Drive while simultaneously lifting an arm
Behavior driveAndLift = new ParallelBehaviorGroup(
    new PurePursuitBehavior(drive, autoPath),
    new LiftToHeightBehavior(lift, Height.HIGH)
);
```

## 4. Conditional Actions

```java
// Only run the intake if a sensor detects an object
Behavior conditionalIntake = new ConditionalBehavior(
    new IntakeBehavior(intake),
    () -> sensor.detectsObject()
);
```
