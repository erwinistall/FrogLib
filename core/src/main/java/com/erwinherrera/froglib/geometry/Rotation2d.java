package com.erwinherrera.froglib.geometry;

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/


import com.erwinherrera.froglib.util.Angle;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * A rotation in a 2d coordinate frame represented a point on the unit circle
 * (cosine and sine).
 */
public class Rotation2d {
    private final Angle m_angle;
    private final double m_cos;
    private final double m_sin;

    /**
     * Constructs a Rotation2d with a default angle of 0 degrees.
     */
    public Rotation2d() {
        this(Angle.fromRadians(0.0));
    }

    /**
     * Constructs a Rotation2d with the given radian value.
     *
     * @param value The value of the angle in radians.
     */
    public Rotation2d(double value) {
        this(Angle.fromRadians(value));
    }

    /**
     * Constructs a Rotation2d with the given Angle.
     *
     * @param angle The angle.
     */
    public Rotation2d(Angle angle) {
        m_angle = angle.wrap();
        m_cos = Math.cos(m_angle.toRadians());
        m_sin = Math.sin(m_angle.toRadians());
    }

    /**
     * Constructs a Rotation2d with the given x and y (cosine and sine)
     * components.
     *
     * @param x The x component or cosine of the rotation.
     * @param y The y component or sine of the rotation.
     */
    @SuppressWarnings("ParameterName")
    public Rotation2d(double x, double y) {
        double magnitude = Math.hypot(x, y);
        if (magnitude > 1e-6) {
            m_sin = y / magnitude;
            m_cos = x / magnitude;
        } else {
            m_sin = 0.0;
            m_cos = 1.0;
        }
        m_angle = Angle.fromRadians(Math.atan2(m_sin, m_cos));
    }

    /**
     * Constructs and returns a Rotation2d with the given degree value.
     *
     * @param degrees The value of the angle in degrees.
     * @return The rotation object with the desired angle value.
     */
    public static Rotation2d fromDegrees(double degrees) {
        return new Rotation2d(Angle.fromDegrees(degrees));
    }

    /**
     * Adds two rotations together.
     *
     * @param other The rotation to add.
     * @return The sum of the two rotations.
     */
    public Rotation2d plus(Rotation2d other) {
        return rotateBy(other);
    }

    /**
     * Subtracts the new rotation from the current rotation.
     *
     * @param other The rotation to subtract.
     * @return The difference between the two rotations.
     */
    public Rotation2d minus(Rotation2d other) {
        return rotateBy(other.unaryMinus());
    }

    /**
     * Takes the inverse of the current rotation.
     *
     * @return The inverse of the current rotation.
     */
    public Rotation2d unaryMinus() {
        return new Rotation2d(m_angle.times(-1.0));
    }

    /**
     * Multiplies the current rotation by a scalar.
     *
     * @param scalar The scalar.
     * @return The new scaled Rotation2d.
     */
    public Rotation2d times(double scalar) {
        return new Rotation2d(m_angle.times(scalar));
    }

    /**
     * Adds the new rotation to the current rotation using a rotation matrix.
     *
     * @param other The rotation to rotate by.
     * @return The new rotated Rotation2d.
     */
    public Rotation2d rotateBy(Rotation2d other) {
        return new Rotation2d(
                m_cos * other.m_cos - m_sin * other.m_sin,
                m_cos * other.m_sin + m_sin * other.m_cos
        );
    }

    /**
     * Returns the radian value of the rotation.
     *
     * @return The radian value of the rotation.
     */
    public double getRadians() {
        return m_angle.toRadians();
    }

    /**
     * Returns the degree value of the rotation.
     *
     * @return The degree value of the rotation.
     */
    public double getDegrees() {
        return m_angle.toDegrees();
    }

    /**
     * Returns the angle as an Angle object.
     *
     * @return The angle.
     */
    public Angle getAngle() {
        return m_angle;
    }

    /**
     * Returns the angle value of the rotation in the specified angle unit.
     *
     * @param angleUnit The angle unit of the value to be returned.
     * @return The value of the rotation.
     */
    public double getAngle(AngleUnit angleUnit) {
        return m_angle.to(angleUnit);
    }

    /**
     * Returns the cosine of the rotation.
     *
     * @return The cosine of the rotation.
     */
    public double getCos() {
        return m_cos;
    }

    /**
     * Returns the sine of the rotation.
     *
     * @return The sine of the rotation.
     */
    public double getSin() {
        return m_sin;
    }

    /**
     * Returns the tangent of the rotation.
     *
     * @return The tangent of the rotation.
     */
    public double getTan() {
        return m_sin / m_cos;
    }

    @Override
    public String toString() {
        return String.format("Rotation2d(%s)", m_angle.toString());
    }

    /**
     * Checks equality between this Rotation2d and another object.
     *
     * @param obj The other object.
     * @return Whether the two objects are equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rotation2d) {
            return m_angle.equals(((Rotation2d) obj).m_angle);
        }
        return false;
    }
}
