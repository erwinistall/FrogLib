package com.erwinherrera.froglib.behaviors;

/**
 * Schedules a Behavior as uninterruptible
 * @author Erwin Herrera
 */
    public class UninterruptibleBehavior extends BehaviorBase {
    private final Behavior Behavior;

    /**
     * @param Behavior the Behavior to be schedules as uninterruptible
     * This expects a single Behavior, so multiple Behaviors need to be put in a
     * BehaviorGroup first:
     * {@link SequentialBehaviorGroup}
     * {@link ParallelBehaviorGroup}
     */
    public UninterruptibleBehavior(Behavior Behavior) {
        this.Behavior = Behavior;
    }

    @Override
    public void onDive() {
        Behavior.schedule(false);
    }

    @Override
    public boolean hasSurfaced() {
        return !BehaviorManager.getInstance().isScheduled(Behavior);
    }
}
