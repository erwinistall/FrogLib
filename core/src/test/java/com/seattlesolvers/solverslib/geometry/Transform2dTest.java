package com.seattlesolvers.solverslib.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Transform2dTest {
    private static final double kEpsilon = 1E-9;
    Pose2d initial = new Pose2d(new Translation2d(1.0, 2.0), Rotation2d.fromDegrees(45.0));
    Transform2d base = new Transform2d(new Translation2d(10, 10), new Rotation2d(1));

    @Test
    public void testInverse() {
        Transform2d transformation = new Transform2d(new Translation2d(5.0, 0.0),
                Rotation2d.fromDegrees(5.0));

        Pose2d transformed = initial.plus(transformation);
        Pose2d untransformed = transformed.plus(transformation.inverse());

        assertEquals(initial.getX(), untransformed.getX(), kEpsilon);
        assertEquals(initial.getY(), untransformed.getY(), kEpsilon);
        assertEquals(initial.getRotation().getDegrees(), untransformed.getRotation().getDegrees(), kEpsilon);
    }

    @Test
    public void times() {
        assertEquals(new Transform2d(new Translation2d(20, 20), new Rotation2d(2)), base.times(2));
    }

    @Test
    public void getTranslation() {
        assertEquals(new Translation2d(10, 10), base.getTranslation());
    }

    @Test
    public void getRotation() {
        assertEquals(new Rotation2d(1), base.getRotation());
    }

    @Test
    public void testEquals() {
        assertEquals(base, new Transform2d(new Translation2d(10, 10), new Rotation2d(1)));
    }

}