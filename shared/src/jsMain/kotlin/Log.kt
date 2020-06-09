package com.racepartyapp.shared

import kotlin.reflect.KClass

actual object LogFactory {
    actual fun getLog(name: String): Log {
        return Log(name)
    }

    actual fun getLog(clazz: KClass<*>): Log {
        return Log(clazz.simpleName!!)
    }
}

actual class Log(val name: String) {
    actual fun debug(str: String) {
        console.log("$name: $str")
    }

    actual fun info(str: String) {
        console.info("$name: $str")
    }

    actual fun warn(str: String) {
        console.warn("$name: $str")
    }

    @Suppress("UnsafeCastFromDynamic") // Comment out this line to reproduce KT-39421
    actual fun error(str: String, t: Throwable?) {
        console.error("$name: $str" + (if (t == null) "" else ": " + t.asDynamic().stack))
    }
}