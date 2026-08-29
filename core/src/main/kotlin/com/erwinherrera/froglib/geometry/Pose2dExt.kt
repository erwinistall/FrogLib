package com.erwinherrera.froglib.geometry

import com.erwinherrera.froglib.geometry.Pose2d
import com.erwinherrera.froglib.geometry.Transform2d
import com.erwinherrera.froglib.geometry.Twist2d

/**
 * @author Erwin Herrera
 *
 * Adding quality for life updates to transformBy, relativeTo, exp, log, and rotate to be able to be
 * called in infix notation and to also take in parameters as Numbers to eliminate the need to
 * constantly do .toDouble() on inputs that are not Doubles
 */
infix fun Pose2d.transformBy(other: Transform2d): Pose2d =
        this.transformBy(other)

infix fun Pose2d.relativeTo(other: Pose2d): Pose2d =
        this.relativeTo(other)

infix fun Pose2d.exp(twist2d: Twist2d): Pose2d =
        this.exp(twist2d)

infix fun Pose2d.log(end: Pose2d): Twist2d =
        this.log(end)

infix fun Pose2d.rotate(dT: Number): Pose2d =
        this.rotate(dT.toDouble())
