/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import android.util.Log;

/**
 * A Behavior that logs a message when initialized.
 */
public class LogCatBehavior extends InstantBehavior {

    public LogCatBehavior(String tag, String message, int priority) {
        super(() -> Log.println(priority, tag, message));
    }

    public LogCatBehavior(String tag, String message) {
        this(tag, message, Log.DEBUG);
    }

    public LogCatBehavior(String message) {
        this("LogCatBehavior", message);
    }


    public LogCatBehavior(String message, int priority) {
        this("LogCatBehavior", message, priority);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}
