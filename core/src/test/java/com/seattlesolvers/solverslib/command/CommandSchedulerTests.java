package com.seattlesolvers.solverslib.command;

import static org.junit.Assert.assertEquals;

import com.seattlesolvers.solverslib.command.button.Trigger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandSchedulerTests {

    public static int x = 3;
    private boolean val = false;

    @Before
    public void setup() {
        x = 3;
        val = false;
        Robot.enable();
    }

    @After
    public void teardown() {
        CommandScheduler.getInstance().reset();
    }

    @Test
    public void testToggleBetweenCommands() {
        Trigger toggler = new Trigger(this::getValue).toggleWhenActive(
                new InstantCommand(() -> x = 5),
                new InstantCommand(() -> x = 3)
        );
        assertEquals(3, x);
        CommandScheduler.getInstance().run();
        assertEquals("The value of x should have updated", 3, x);

        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals(5, x);
        CommandScheduler.getInstance().run();
        assertEquals("The value of x should not have updated", 5, x);

        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals("The value of x should not have updated", 5, x);
        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals("The value of x should have updated", 3, x);

        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals("The value of should not have updated", 3, x);
        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals("The value of x should have updated", 5, x);

        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals("The value of should not have updated", 5, x);
        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals("The value of x should have updated", 3, x);
    }

    @Test
    public void testAddCommand() {
        CommandScheduler.getInstance().schedule(new CommandBase() {
            @Override
            public void execute() {
                x = 5;
            }

            @Override
            public boolean runsWhenDisabled() {
                return false;
            }
        });

        CommandScheduler.getInstance().run();
        assertEquals(5, x);
    }

    @Test
    public void testNotRunWhenDisabled() {
        Robot.disable();
        CommandScheduler.getInstance().schedule(new CommandBase() {
            @Override
            public void execute() {
                x = 5;
            }

            @Override
            public boolean runsWhenDisabled() {
                return false;
            }
        });

        CommandScheduler.getInstance().run();
        assertEquals(3, x);
    }

    @Test
    public void testSubsystemPeriodic() {
        CommandScheduler.getInstance().registerSubsystem(new SubsystemBase() {
            @Override
            public void periodic() {
                x = 5;
            }
        });

        CommandScheduler.getInstance().run();
        assertEquals(5, x);
    }

    @Test
    public void pollButtons() {
        Trigger button = new Trigger(this::getValue).whenActive(new CommandBase() {
            @Override
            public void execute() {
                x = 5;
            }

            @Override
            public boolean runsWhenDisabled() {
                return false;
            }
        });
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        updateValue();
        CommandScheduler.getInstance().run();
        assertEquals(5, x);
    }

    @Test
    public void testDefaultCommand() {
        SubsystemBase p = new SubsystemBase() {
            @Override
            public void periodic() {
                x = 3;
            }
        };
        p.register();
        p.setDefaultCommand(new RunCommand(() -> x = 5, p));
        assertEquals(3, x);
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        CommandScheduler.getInstance().run();
        assertEquals(5, x);
        CommandScheduler.getInstance().schedule(new InstantCommand(() -> x = 3, p));
        x = 3;
        assertEquals(3, x);
        CommandScheduler.getInstance().run();
        assertEquals(3, x);
        CommandScheduler.getInstance().run();
        assertEquals(5, x);
        CommandScheduler.getInstance().schedule(new RunCommand(() -> x = 4, p));
        CommandScheduler.getInstance().run();
        assertEquals(4, x);
        CommandScheduler.getInstance().run();
        assertEquals(4, x);
    }

    public boolean getValue() {
        return val;
    }

    private void updateValue() {
        val = !val;
    }

}
