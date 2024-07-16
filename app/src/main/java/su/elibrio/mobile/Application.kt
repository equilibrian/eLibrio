package su.elibrio.mobile

import android.app.Application
import android.os.Environment
import su.elibrio.mobile.utils.FileLoggingTree
import timber.log.Timber
import timber.log.Timber.Forest.plant
import java.io.File

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        val logDir = File(Environment.getExternalStorageDirectory(), "eLibrio/logs")
        System.setProperty("LOG_DIR", logDir.absolutePath)

        plant(FileLoggingTree())
    }
}