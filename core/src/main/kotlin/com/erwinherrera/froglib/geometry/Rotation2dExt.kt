package com.erwinherrera.froglib.geometry

import com.erwinherrera.froglib.geometry.Rotation2d

/**
 * @author Erwin Herrera
 *
 * Add quality of life update to be able to call rotateBy in infix notation
 */
infix fun Rotation2d.rotateBy(other: Rotation2d): Rotation2d =
        this.rotateBy(other)
