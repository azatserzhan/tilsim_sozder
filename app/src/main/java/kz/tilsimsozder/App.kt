package kz.tilsimsozder

import android.app.Application
import kz.tilsimsozder.prayers.PrayerModule
import kz.tilsimsozder.tilsim.TilsimModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(PrayerModule.create())
            modules(TilsimModule.create())
        }
    }
}