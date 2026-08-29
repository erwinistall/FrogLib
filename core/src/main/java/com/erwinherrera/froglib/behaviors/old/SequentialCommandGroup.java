package com.erwinherrera.froglib.behaviors.old;

import java.util.ArrayList;

/**
 * Allows you to combine multiple Behaviors into one.
 */
@Deprecated
public abstract class SequentialBehaviorGroup implements Behavior {

    private ArrayList<Behavior> Behaviors;
    private int numBehaviorsFinished;
    private int totalNumBehaviors;
    private int currentBehavior;

    private void addBehavior(Behavior Behavior) {
        Behaviors.add(Behavior);
    }

    @Override
    public void onDive() {
        totalNumBehaviors = Behaviors.size();
        numBehaviorsFinished = 0;
        currentBehavior = 0;
        Behaviors.get(currentBehavior).onDive();
    }

    @Override
    public void onPaddle() {
        Behaviors.get(currentBehavior).onPaddle();

        if (Behaviors.get(currentBehavior).hasSurfaced()) {
            Behaviors.get(currentBehavior).onFly();
            currentBehavior++;
            numBehaviorsFinished++;
            if (numBehaviorsFinished < totalNumBehaviors) {
                Behaviors.get(currentBehavior).onDive();
            }
        }

    }

    @Override
    public void onFly() {

    }

    @Override
    public boolean hasSurfaced() {
        return numBehaviorsFinished == totalNumBehaviors;
    }
}
