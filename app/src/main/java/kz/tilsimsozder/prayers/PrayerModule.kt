package kz.tilsimsozder.prayers

import kz.azatserzhanov.test.common.InjectionModule
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.presenter.PrayersPresenter
import org.koin.dsl.module

object PrayerModule : InjectionModule {
    override fun create() = module {
        single { PrayersPresenter(get(), get()) }
        single { Analytics() }
    }
}