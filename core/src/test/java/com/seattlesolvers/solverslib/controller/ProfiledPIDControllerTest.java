package com.seattlesolvers.solverslib.controller;

import static org.junit.Assert.assertEquals;

import com.seattlesolvers.solverslib.controller.wpilibcontroller.ProfiledPIDController;
import com.seattlesolvers.solverslib.trajectory.TrapezoidProfile;

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