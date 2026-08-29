package com.seattlesolvers.solverslib.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class Rotation2dTest {
    private static final double kEpsilon = 1E-9;

    @Test
    public void testRadiansToDegrees() {
        Rotation2d one = new Rotation2d(Math.PI / 3);
        Rotation2d two = new Rotation2d(Math.PI / 4);

        assertEquals(one.getDegrees(), 60.0, kEpsilon);
        assertEquals(two.getDegrees(), 45.0, kEpsilon);
    }

    @Test
    public void testRadiansAndDegrees() {
        Rotation2d one = Rotation2d.fromDegrees(45.0);
        Rotation2d two = Rotation2d.fromDegrees(30.0);

        assertEquals(one.getRadians(), Math.PI / 4, kEpsilon);
        assertEquals(two.getRadians(), Math.PI / 6, kEpsilon);
    }

    @Test
    public void testRotateByFromZero() {
        Rotation2d zero = new Rotation2d();
        Rotation2d rotated = zero.rotateBy(Rotation2d.fromDegrees(90.0));

        assertEquals(rotated.getRadians(), Math.PI / 2.0, kEpsilon);
        assertEquals(rotated.getDegrees(), 90.0, kEpsilon);
    }

    @Test
    public void testRotateByNonZero() {
        Rotation2d rot = Rotation2d.fromDegrees(90.0);
        rot = rot.plus(Rotation2d.fromDegrees(30.0));

        assertEquals(rot.getDegrees(), 120.0, kEpsilon);
    }

    @Test
    public void testMinus() {
        Rotation2d one = Rotation2d.fromDegrees(70.0);
        Rotation2d two = Rotation2d.fromDegrees(30.0);

        assertEquals(one.minus(two).getDegrees(), 40.0, kEpsilon);
    }

    @Test
    public void testEquality() {
        Rotation2d one = Rotation2d.fromDegrees(43.0);
        Rotation2d two = Rotation2d.fromDegrees(43.0);
        assertEquals(one, two);
    }

    @Test
    public void testInequality() {
        Rotation2d one = Rotation2d.fromDegrees(43.0);
        Rotation2d two = Rotation2d.fromDegrees(43.5);
        assertNotEquals(one, two);
    }
}
