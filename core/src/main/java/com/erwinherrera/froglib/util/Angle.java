package com.erwinherrera.froglib.util;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * A utility class representing an angle, allowing for seamless interchange between
 * degrees and radians.
 *
 * @author Erwin Herrera
 */
public class Angle {
    private final double radians;

    private Angle(double radians) {
        this.radians = radians;
    }

    /**
     * Create an Angle from a value in radians.
     */
    public static Angle fromRadians(double radians) {
        return new Angle(radians);
    }

    /**
     * Create an Angle from a value in degrees.
     */
    public static Angle fromDegrees(double degrees) {
        return new Angle(Math.toRadians(degrees));
    }

    /**
     * Create an Angle from a value in a specified unit.
     */
    public static Angle from(double value, AngleUnit unit) {
        return unit == AngleUnit.RADIANS ? fromRadians(value) : fromDegrees(value);
    }

    /**
     * Get the angle value in radians.
     */
    public double toRadians() {
        return radians;
    }

    /**
     * Get the angle value in degrees.
     */
    public double toDegrees() {
        return Math.toDegrees(radians);
    }

    /**
     * Get the angle value in the specified unit.
     */
    public double to(AngleUnit unit) {
        return unit == AngleUnit.RADIANS ? toRadians() : toDegrees();
    }

    /**
     * Normalizes the angle to be within [-pi, pi] or [-180, 180].
     */
    public Angle wrap() {
        return fromRadians(MathUtils.normalizeRadians(radians, false));
    }

    /**
     * Normalizes the angle to be within [0, 2pi] or [0, 360].
     */
    public Angle wrapPositive() {
        return fromRadians(MathUtils.normalizeRadians(radians, true));
    }

    public Angle plus(Angle other) {
        return fromRadians(this.radians + other.radians);
    }

    public Angle minus(Angle other) {
        return fromRadians(this.radians - other.radians);
    }

    public Angle times(double scalar) {
        return fromRadians(this.radians * scalar);
    }

    @Override
    public String toString() {
        return String.format("%.2f deg (%.2f rad)", toDegrees(), toRadians());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Angle) {
            return Math.abs(((Angle) obj).radians - radians) < 1e-9;
        }
        return false;
    }
}
