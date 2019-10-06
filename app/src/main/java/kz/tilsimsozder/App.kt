package kz.tilsimsozder

import android.app.Application
import kz.tilsimsozder.bots.BotModule
import kz.tilsimsozder.news.NewsModule
import kz.tilsimsozder.prayers.PrayerModule
import kz.tilsimsozder.preference.PreferenceModule
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
            modules(NewsModule.create())
            modules(BotModule.create())
            modules(PreferenceModule.create())
        }
    }
}