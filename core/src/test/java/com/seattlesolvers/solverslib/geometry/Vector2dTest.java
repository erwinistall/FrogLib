package com.seattlesolvers.solverslib.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Vector2dTest {

    Vector2d vec = new Vector2d(1, 0);

    @Test
    public void testRotateBy() {
        // see https://en.wikipedia.org/wiki/Machine_epsilon for the delta, we'll be more giving here
        assertEquals(Math.PI / 2, vec.rotateBy(Math.toDegrees(Math.PI / 2)).angle(), 2.22e-16);
    }
}
