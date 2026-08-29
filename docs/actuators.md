# Actuators and Sensors

FrogLib provides powerful wrappers for FTC hardware, making it easier to control your robot's movements.

## Motors: Swimming and Anchoring

The `Motor` class is the foundation for motion. It abstracts away the complexities of the SDK's `DcMotor` and adds advanced control features.

### `swim(double power)`
Use `swim()` to set the motor's power or velocity. Depending on the `RunMode`, this might be raw power, velocity-controlled power, or position-controlled power.

```java
motor.swim(0.5); // Start swimming at half power
```

### `anchor()`
Use `anchor()` to immediately stop the motor. It's like dropping an anchor in the pond—motion ceases instantly.

```java
motor.anchor(); // Stop moving
```

## MotorEx
`MotorEx` extends the standard motor with even more features, such as built-in PIDF control and profile-based movement.

## Servos
The `ServoEx` class provides a cleaner API for servos, including:
- Angle-based positioning.
- Caching to reduce loop times.
- Range scaling.

## Sensors
FrogLib includes wrappers for common sensors:
- `SensorDistance`: For TOF and ultrasonic distance sensors.
- `SensorColor`: For color and light sensors.
- `RevIMU`: A robust wrapper for the Hub's built-in IMU.
