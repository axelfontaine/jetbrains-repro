package com.racepartyapp.shared

import kotlin.reflect.KClass

expect object LogFactory {
    fun getLog(name: String): Log
    fun getLog(clazz: KClass<*>): Log
}

expect class Log {
    fun debug(str: String)
    fun info(str: String)
    fun warn(str: String)
    fun error(str: String, t: Throwable? = null)
}