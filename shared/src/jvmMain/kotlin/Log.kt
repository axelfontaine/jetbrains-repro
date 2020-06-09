package com.racepartyapp.shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

actual object LogFactory {
    actual fun getLog(name: String): Log {
        return Log(LoggerFactory.getLogger(name))
    }

    actual fun getLog(clazz: KClass<*>): Log {
        return Log(LoggerFactory.getLogger(clazz.java))
    }
}

actual class Log(val logger: Logger) {
    actual fun debug(str: String) {
        logger.debug(str)
    }

    actual fun info(str: String) {
        logger.info(str)
    }

    actual fun warn(str: String) {
        logger.warn(str)
    }

    actual fun error(str: String, t: Throwable?) {
        logger.error(str, t)
    }
}