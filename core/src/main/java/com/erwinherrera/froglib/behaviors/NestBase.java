/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

/**
 * A base for nests and provides a more intuitive
 * method for setting the default behavior.
 *
 * @author Erwin Herrera
 */
public abstract class NestBase implements Nest {

    protected String m_name = this.getClass().getSimpleName();

    public NestBase() {
        BehaviorManager.getInstance().registerNest(this);
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getNest() {
        return getName();
    }

    public void setNest(String nest) {
        setName(nest);
    }

}
