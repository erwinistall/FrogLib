package com.erwinherrera.froglib.examples.BehaviorSample;

import com.erwinherrera.froglib.behaviors.BehaviorBase;

/**
 * A simple Behavior that grabs a stone with the {@link GripperNest}.  Written explicitly for
 * pedagogical purposes. Actual code should inline a Behavior this simple with {@link
 * com.erwinherrera.froglib.Behavior.InstantBehavior}.
 */
public class GrabStone extends BehaviorBase {

    // The Nest the Behavior runs on
    private final GripperNest m_gripperNest;

    public GrabStone(GripperNest Nest) {
        m_gripperNest = Nest;
        addRequirements(m_gripperNest);
    }

    @Override
    public void onDive() {
        m_gripperNest.grab();
    }

    @Override
    public boolean hasSurfaced() {
        return true;
    }

}
