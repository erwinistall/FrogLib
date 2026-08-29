package com.seattlesolvers.solverslib.gamepad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.junit.Before;
import org.junit.Test;

import java.util.function.BooleanSupplier;

public class GamepadButtonTest {

    public static int x = 3;
    private Gamepad myGamepad;
    private GamepadEx gamepadEx;

    @Before
    public void setup() {
        myGamepad = new Gamepad();
        gamepadEx = new GamepadEx(myGamepad);
    }

    @Test
    public void simpleTest() {
        myGamepad.a = false;
        assertFalse(gamepadEx.getButton(GamepadKeys.Button.A));
        myGamepad.a = true;
        assertTrue(gamepadEx.getButton(GamepadKeys.Button.A));
    }

    @Test
    public void oneButtonTest() {
        myGamepad.a = false;
        assertFalse(gamepadEx.getButton(GamepadKeys.Button.A));
        gamepadEx.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(() -> x = 5));
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        myGamepad.a = true;
        CommandScheduler.getInstance().run();
        assertEquals(5, x);
        CommandScheduler.getInstance().reset();
        x = 3;
    }

    @Test
    public void whenPressedTest() {
        myGamepad.a = false;
        BooleanSupplier wasJustPressed = () -> gamepadEx.wasJustPressed(GamepadKeys.Button.A);
        assertFalse(wasJustPressed.getAsBoolean());
        myGamepad.a = true;
        assertFalse(wasJustPressed.getAsBoolean());
        gamepadEx.readButtons();
        assertTrue(wasJustPressed.getAsBoolean());
        myGamepad.a = true;
        assertTrue(wasJustPressed.getAsBoolean());
        gamepadEx.readButtons();
        assertFalse(wasJustPressed.getAsBoolean());
        myGamepad.a = false;
        assertFalse(wasJustPressed.getAsBoolean());
        gamepadEx.readButtons();
        assertFalse(wasJustPressed.getAsBoolean());
    }

    @Test
    public void twoButtonCommandTest() {
        myGamepad.a = false;
        myGamepad.b = false;
        assertFalse(gamepadEx.getButton(GamepadKeys.Button.B));
        gamepadEx.getGamepadButton(GamepadKeys.Button.A)
                .and(gamepadEx.getGamepadButton(GamepadKeys.Button.B).negate())
                .whenActive(new InstantCommand(() -> x = 5));
        gamepadEx.getGamepadButton(GamepadKeys.Button.B)
                .and(gamepadEx.getGamepadButton(GamepadKeys.Button.A).negate())
                .whenActive(new InstantCommand(() -> x = 3));
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        myGamepad.a = true;
        myGamepad.b = true;
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        myGamepad.a = true;
        myGamepad.b = false;
        CommandScheduler.getInstance().run();
        assertEquals(5, x);
        myGamepad.a = false;
        myGamepad.b = true;
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        myGamepad.a = false;
        myGamepad.b = false;
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        CommandScheduler.getInstance().reset();
    }

}
