package com.seattlesolvers.solverslib.hardware.motors;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;

public class EncoderTest {

    private MockMotor motor;
    private MockMotor.MockEncoder encoder;

    public static final double CONSTANT = 10;

    @Test
    @Before
    public void testCreateEncoder() {
        motor = new MockMotor();
        encoder = motor.encoder.setDistancePerPulse(CONSTANT);
    }

    @Test
    public void testGetValue() {
        assertEquals(0, encoder.getPosition());
        assertEquals(1, encoder.getPosition());
        // see https://en.wikipedia.org/wiki/Machine_epsilon
        assertEquals(20, encoder.getDistance(), 2.22e-16);
        assertEquals(3, motor.getCurrentPosition());
    }

    private static class MockMotor {
        private int position;
        private MockEncoder encoder;

        private static class MockEncoder {
            private Supplier<Integer> m_supplier;
            private double dpp;

            public MockEncoder(Supplier<Integer> supplier) {
                m_supplier = supplier;
            }

            public int getPosition() {
                return m_supplier.get();
            }

            public MockEncoder setDistancePerPulse(double distancePerPulse) {
                dpp = distancePerPulse;
                return this;
            }

            public double getDistance() {
                return dpp * getPosition();
            }
        }

        public MockMotor() {
            encoder = new MockEncoder(this::getCurrentPosition);
        }

        public int getCurrentPosition() {
            return position++;
        }
    }

}
