package com.erwinherrera.froglib.controller;

import static org.junit.Assert.assertEquals;

import com.erwinherrera.froglib.controller.wpilibcontroller.ProfiledPIDController;
import com.erwinherrera.froglib.trajectory.TrapezoidProfile;

import org.junit.Test;

public class ProfiledPIDControllerTest {
    @Test
    public void testStartFromNonZeroPosition() {
        ProfiledPIDController controller = new ProfiledPIDController(1.0, 0.0, 0.0,
                new TrapezoidProfile.Constraints(1.0, 1.0));

        controller.reset(20);

        assertEquals(0, controller.calculate(20, 20), 0.05);
    }
}
