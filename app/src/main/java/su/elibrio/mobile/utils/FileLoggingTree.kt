package su.elibrio.mobile.utils

import android.util.Log
import org.slf4j.LoggerFactory
import timber.log.Timber


class FileLoggingTree : Timber.Tree() {
    private val logger = LoggerFactory.getLogger({}::class.java)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.VERBOSE -> logger.trace(message)
            Log.DEBUG -> logger.debug(message)
            Log.INFO -> logger.info(message)
            Log.WARN -> logger.warn(message)
            Log.ERROR -> logger.error(message, t)
            else -> logger.debug(message)
        }
    }
}