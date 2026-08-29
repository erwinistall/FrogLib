/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.erwinherrera.froglib.behaviors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A base class for {@link Behavior}s.
 *
 * @author Erwin Herrera
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class BehaviorBase implements Behavior {

    protected String m_name = this.getClass().getSimpleName();
    protected String m_nest = "Ungrouped";
    protected Set<Nest> m_requirements = new HashSet<>();

    /**
     * Adds the specified requirements to the behavior.
     *
     * @param requirements the requirements to add
     */
    public BehaviorBase addRequirements(Nest... requirements) {
        m_requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    @Override
    public Set<Nest> getRequirements() {
        return m_requirements;
    }

    public String getName() {
        return m_name;
    }

    public BehaviorBase setName(String name) {
        m_name = name;
        return this;
    }

    public String getNest() {
        return m_nest;
    }

    public BehaviorBase setNest(String nest) {
        m_nest = nest;
        return this;
    }

}
