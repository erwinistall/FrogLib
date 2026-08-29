/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

/**
 * A Behavior that prints a string when started.
 */
public class PrintBehavior extends InstantBehavior {
    /**
     * Creates a new a PrintBehavior.
     *
     * @param message the message to print
     */
    public PrintBehavior(String message) {
        super(() -> System.out.println(message));
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
