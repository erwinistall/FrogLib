package com.erwinherrera.froglib.util

import com.erwinherrera.froglib.util.LUT

/**
 * @author Erwin Herrera
 *
 * Even though LUT extends HashMap, there is no function to treat a Map<T: Number, R> as a
 * LUT<T: Number, R>. Extension function allows for easy conversion from Map<T: Number, R> to a
 * LUT<T: Number, R>.
 */
fun <T, R> Map<T, R>.toLUT(): LUT<T, R> where T : Number {
    val ret = LUT<T, R>()
    for (i in this) {
        ret[i.key] = i.value
    }
    return ret
}
