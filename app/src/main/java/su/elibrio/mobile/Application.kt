package su.elibrio.mobile

import android.app.Application
import timber.log.Timber
import timber.log.Timber.Forest.plant

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}