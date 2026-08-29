package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.BehaviorBase;

/**
 * A simple Behavior that releases a stone with the {@link GripperNest}.  Written explicitly for
 * pedagogical purposes. Actual code should inline a Behavior this simple with {@link
 * com.erwinherrera.froglib.Behavior.InstantBehavior}.
 */
public class ReleaseStone extends BehaviorBase {

    // The Nest the Behavior runs on
    private final GripperNest m_gripperNest;

    public ReleaseStone(GripperNest Nest) {
        m_gripperNest = Nest;
        addRequirements(m_gripperNest);
    }

    @Override
    public void onDive() {
        m_gripperNest.release();
    }

    @Override
    public boolean hasSurfaced() {
        return true;
    }

}
