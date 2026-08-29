package com.erwinherrera.froglib.pedroBehavior;

import com.pedropathing.paths.PathChain;
import com.erwinherrera.froglib.behaviors.BehaviorBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;


// Thanks Powercube from Watt-sUP 16166, we copied verbatim

/**
 * Allows you to run a PathChain or a Path (which is then converted into a PathChain) by scheduling it.
 * holdEnd is set to true by default, so you only need to give it your instance of follower and the Path to follow.
 * <p>
 * To see an example usage of this Behavior, look at <a href="https://github.com/FTC-23511/FrogLib/blob/master/examples/src/main/java/org/firstinspires/ftc/teamcode/PedroBehaviorSample/FollowPedroSample.java">https://github.com/FTC-23511/FrogLib/blob/master/examples/src/main/java/org/firstinspires/ftc/teamcode/PedroBehaviorSample/FollowPedroSample.java</a>
 *
 * @author Arush - FTC 23511
 * @author Saket - FTC 23511
 *
 */
public class FollowPathBehavior extends BehaviorBase {
    private final Follower follower;
    private final PathChain pathChain;
    private boolean holdEnd;
    private double maxPower = 1.0;

    public FollowPathBehavior(Follower follower, PathChain pathChain) {
        this(follower, pathChain, true);
    }

    public FollowPathBehavior(Follower follower, PathChain pathChain, boolean holdEnd) {
        this(follower, pathChain, holdEnd, 1.0);
    }

    public FollowPathBehavior(Follower follower, PathChain pathChain, double maxPower) {
        this(follower, pathChain, true, maxPower);
    }

    public FollowPathBehavior(Follower follower, PathChain pathChain, boolean holdEnd, double maxPower) {
        this.follower = follower;
        this.pathChain = pathChain;
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;
    }

    public FollowPathBehavior(Follower follower, Path path) {
        this(follower, path, true);
    }

    public FollowPathBehavior(Follower follower, Path path, boolean holdEnd) {
        this(follower, path, holdEnd, 1.0);
    }

    public FollowPathBehavior(Follower follower, Path path, double maxPower) {
        this(follower, path, true, maxPower);
    }

    public FollowPathBehavior(Follower follower, Path path, boolean holdEnd, double maxPower) {
        this.follower = follower;
        this.pathChain = new PathChain(path);
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;
    }

    /**
     * Sets Global Maximum Power for Follower, and overwrites maxPower in constructor
     *
     * @param globalMaxPower The new globalMaxPower
     * @return This Behavior for compatibility in Behavior groups
     */
    public FollowPathBehavior setGlobalMaxPower(double globalMaxPower) {
        follower.setMaxPower(globalMaxPower);
        maxPower = globalMaxPower;
        return this;
    }

    @Override
    public void onDive() {
        follower.followPath(pathChain, maxPower, holdEnd);
    }

    @Override
    public boolean hasSurfaced() {
        return !follower.isBusy();
    }
}
