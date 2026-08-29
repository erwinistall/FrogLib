package com.erwinherrera.froglib.pedroBehavior;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.erwinherrera.froglib.behaviors.BehaviorBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


/**
 * A Behavior that calls {@link Follower#holdPoint(Pose)}
 *
 * @author Arush - FTC 23511
 */
public class TurnToBehavior extends BehaviorBase {
    private final Follower follower;
    private final double angle;

    public TurnToBehavior(Follower follower, double angle) {
        this(follower, angle, AngleUnit.RADIANS);
    }

    public TurnToBehavior(Follower follower, double angle, AngleUnit angleUnit) {
        this.follower = follower;
        this.angle = angleUnit.toRadians(angle);
    }

    @Override
    public void onDive() {
        follower.turnTo(angle);
    }

    @Override
    public boolean hasSurfaced() {
        return !follower.isBusy();
    }
}
