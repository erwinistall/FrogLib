package com.erwinherrera.froglib.pedroBehavior;

import com.pedropathing.follower.Follower;
import com.erwinherrera.froglib.behaviors.BehaviorBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


/**
 * A Behavior that calls {@link Follower#turn(double, boolean)}
 *
 * @author Arush - FTC 23511
 */
public class TurnBehavior extends BehaviorBase {
    private final Follower follower;
    private final double angle;
    private final boolean isLeft;

    public TurnBehavior(Follower follower, double angle, boolean isLeft) {
        this(follower, angle, isLeft, AngleUnit.RADIANS);
    }

    public TurnBehavior(Follower follower, double angle, boolean isLeft, AngleUnit angleUnit) {
        this.follower = follower;
        this.angle = angleUnit.toRadians(angle);
        this.isLeft = isLeft;
    }

    @Override
    public void onDive() {
        follower.turn(angle, isLeft);
    }

    @Override
    public boolean hasSurfaced() {
        return !follower.isBusy();
    }
}
