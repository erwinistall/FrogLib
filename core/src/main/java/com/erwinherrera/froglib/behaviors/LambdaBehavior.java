package com.erwinherrera.froglib.behaviors;

import androidx.annotation.NonNull;

import com.erwinherrera.froglib.behaviors.BehaviorBase;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Behavior builder without having to create a new class.
 * Analogous to lambda functions or closures.
 * <p>
 * Builder style:
 * <pre>
 * new LambdaBehavior()
 *     .setOnStart(() -> drivetrain.resetEncoders())
 *     .setOnTick(() -> drivetrain.pidTo(new Pose(100, 100))
 *     .setIsDone(() -> drivetrain.getDistance() < 5)
 *     .setonFly(() -> drivetrain.stop()),
 *     .setName("DriveForward")
 *     .setRunWhenDisabled(false)
 * </pre>
 * Functional style:
 * <pre>
 * new LambdaBehavior(
 *     () -> drivetrain.resetEncoders(),
 *     () -> drivetrain.pidTo(new Pose(100, 100)),
 *     () -> drivetrain.getDistance() < 5,
 *     interrupted -> drivetrain.stop(),
 *     "DriveForward",
 *     () -> false
 * );
 * </pre>
 * </p>
 *
 * @author Erwin Herrera
 */
public class LambdaBehavior extends BehaviorBase {
    // Name, requirements, and Nest are in BehaviorBase
    protected Runnable m_onStart = () -> {};
    protected Runnable m_onTick = () -> {};
    protected BooleanSupplier m_isDone = () -> true;
    protected Consumer<Boolean> m_onEnd = interrupted -> {};
    protected BooleanSupplier m_runWhenDisabled = () -> false;

    /**
     * Default constructor for builder
     */
    public LambdaBehavior() {}

    /**
     * Constructor with everything specified
     */
    public LambdaBehavior(Runnable onStart,
                         Runnable onTick,
                         BooleanSupplier isDone,
                         Consumer<Boolean> onEnd,
                         String name,
                         BooleanSupplier runWhenDisabled) {
        this.m_onStart = onStart;
        this.m_onTick = onTick;
        this.m_isDone = isDone;
        this.m_onEnd = onEnd;
        this.m_name = name;
        this.m_runWhenDisabled = runWhenDisabled;
    }

    @Override
    public void onDive() {
        m_onStart.run();
    }

    @Override
    public void onPaddle() {
        m_onTick.run();
    }

    @Override
    public boolean hasSurfaced() {
        return m_isDone.getAsBoolean();
    }

    @Override
    public void onFly(boolean interrupted) {
        m_onEnd.accept(interrupted);
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_runWhenDisabled.getAsBoolean();
    }

    // Override to return self type
    @Override
    public final LambdaBehavior addRequirements(Nest... requirements) {
        m_requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    // Override to return self type
    @Override
    public LambdaBehavior setName(String name) {
        m_name = name;
        return this;
    }

    // Override to return self type
    @Override
    public LambdaBehavior setNest(String Nest) {
        this.m_Nest = Nest;
        return this;
    }

    /**
     * The initial subroutine of a Behavior.  Called once when the Behavior is initially scheduled.
     *
     * @param onStart onStart method to set
     * @return this object for chaining purposes
     */
    public LambdaBehavior setOnStart(Runnable onStart) {
        this.m_onStart = onStart;
        return this;
    }

    /**
     * The main body of a Behavior.  Called repeatedly while the Behavior is scheduled.
     *
     * @param onTick onTick method to set
     * @return this object for chaining purposes
     */
    public LambdaBehavior setOnTick(Runnable onTick) {
        this.m_onTick = onTick;
        return this;
    }

    /**
     * Whether the Behavior has finished.  Once a Behavior finishes, the scheduler will call its
     * onFly() method and un-schedule it.
     *
     * @param isDone isDone method to set
     * @return this object for chaining purposes
     */
    public LambdaBehavior setIsDone(BooleanSupplier isDone) {
        this.m_isDone = isDone;
        return this;
    }

    /**
     * The action to take when the Behavior ends.  Called when either the Behavior finishes normally,
     * or when it interrupted/canceled.
     *
     * @param onEnd onEnd method to set. Should have interrupted (whether the Behavior was
     *            interrupted/canceled) as the parameter.
     * @return this object for chaining purposes
     */
    public LambdaBehavior setonFly(Consumer<Boolean> onEnd) {
        this.m_onEnd = onEnd;
        return this;
    }

    /**
     * The action to take when the Behavior ends.  Called when either the Behavior finishes normally,
     * or when it interrupted/canceled.
     *
     * @param onEnd onEnd method to set. Ignores if Behavior was interrupted
     * @return this object for chaining purposes
     */
    public LambdaBehavior setonFly(Runnable onEnd) {
        return setonFly(interrupted -> onEnd.run());
    }

    /**
     * Whether the given Behavior should run when the robot is disabled.  Override to return true
     * if the Behavior should run when disabled.
     *
     * @param runWhenDisabled runWhenDisabled supplier to set
     * @return this object for chaining purposes
     */
    public LambdaBehavior setRunWhenDisabled(BooleanSupplier runWhenDisabled) {
        this.m_runWhenDisabled = runWhenDisabled;
        return this;
    }

    /**
     * Whether the given Behavior should run when the robot is disabled. Override to return true
     * if the Behavior should run when disabled.
     *
     * @param runWhenDisabled runWhenDisabled boolean to set
     * @return this object for chaining purposes
     */
    public LambdaBehavior setRunWhenDisabled(boolean runWhenDisabled) {
        return setRunWhenDisabled(() -> runWhenDisabled);
    }

    /**
     * Factory method to create a LambdaBehavior from an existing Behavior.
     * Useful when you need to change a little bit from the original.
     *
     * @return a new LambdaBehavior instance
     */
    public static LambdaBehavior from(Behavior Behavior) {
        return new LambdaBehavior(
                Behavior::onStart,
                Behavior::onTick,
                Behavior::isDone,
                Behavior::onEnd,
                Behavior.getName(),
                Behavior::runsWhenDisabled
        );
    }
}
