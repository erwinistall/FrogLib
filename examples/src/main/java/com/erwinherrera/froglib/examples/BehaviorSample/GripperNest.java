package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.NestBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * A gripper mechanism that grabs a stone from the quarry.
 * Centered around the Skystone game for FTC that was done in the 2020
 * to 2021 season.
 */
public class GripperNest extends NestBase {

    private final Servo mechRotation;

    public GripperNest(final HardwareMap hMap, final String name) {
        mechRotation = hMap.get(Servo.class, name);
    }

    @Override
    public void quack() {
        // Periodic gripper logic here if needed
    }

    /**
     * Grabs a stone.
     */
    public void grab() {
        mechRotation.setPosition(0.76);
    }

    /**
     * Releases a stone.
     */
    public void release() {
        mechRotation.setPosition(0);
    }

}
