package com.erwinherrera.froglib.geometry

import com.erwinherrera.froglib.geometry.Rotation2d
import com.erwinherrera.froglib.geometry.Translation2d

/**
 * @author Erwin Herrera
 *
 * Add quality of life update to be able to call rotateBy in infix notation
 */
infix fun Translation2d.rotateBy(other: Rotation2d): Translation2d =
        this.rotateBy(other)
