package com.erwinherrera.froglib.examples.OldBehaviorSample;

import com.erwinherrera.froglib.behaviors.old.Behavior;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriveForwardBehavior implements Behavior {

    private DriveNest driveNest;
    private ElapsedTime timer;
    private double distance, speed;
    // 4 inches e.g

    public DriveForwardBehavior(DriveNest driveNest, double distance, double speed) {
        this.driveNest = driveNest;
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public void onDive() {
        driveNest.reset();
    }

    @Override
    public void onPaddle() {
        driveNest.driveToPosition((int) distance, speed);
    }


    @Override
    public void onFly() {
        driveNest.reset();
        driveNest.stop();
    }


    @Override
    public boolean hasSurfaced() {
        return driveNest.atTargetPos();
    }
}
