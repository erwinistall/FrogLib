# The Behavior System

In FrogLib, every action your robot performs is a **Behavior**. Whether it's picking up a pixel, extending a slide, or driving to a coordinate, it's all handled by the `Behavior` interface.

## The Anatomy of a Behavior

Every behavior follows a lifecycle that mimics a frog's journey through the water:

- `onDive()`: The initialization phase. Called once when the behavior starts.
- `onPaddle()`: The main execution phase. Called repeatedly while the behavior is active.
- `hasSurfaced()`: The condition for completion. Returns `true` when the behavior is finished.
- `onFly(boolean interrupted)`: The teardown phase. Called when the behavior ends, either naturally or by being interrupted.

### Example: A Simple Wait Behavior

```java
public class WaitBehavior implements Behavior {
    private final long duration;
    private long startTime;

    public WaitBehavior(long duration) {
        this.duration = duration;
    }

    @Override
    public void onDive() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onPaddle() {
        // Just waiting...
    }

    @Override
    public boolean hasSurfaced() {
        return System.currentTimeMillis() - startTime >= duration;
    }
}
```

## The BehaviorManager

The `BehaviorManager` is the brain of the pond. It handles scheduling, requirements, and the main loop. You don't usually interact with it directly, but it ensures that two behaviors don't try to use the same **Nest** at the same time.

## Composing Behaviors

FrogLib allows you to combine behaviors using powerful decorators:

- `andThen(next)`: Run behaviors in sequence.
- `alongWith(other)`: Run behaviors at the same time.
- `raceWith(other)`: Run behaviors in parallel until one finishes.
- `deadlineWith(other)`: Run behaviors in parallel until the main one finishes.
- `withTimeout(millis)`: Force a behavior to finish after a certain time.

```java
myBehavior.withTimeout(1000).andThen(nextBehavior);
```
