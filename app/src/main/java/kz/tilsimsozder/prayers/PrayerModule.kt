package kz.tilsimsozder.prayers

import kz.azatserzhanov.test.common.InjectionModule
import kz.azatserzhanov.test.currency.presenter.PrayersPresenter
import kz.tilsimsozder.firebase.Analytics
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PrayerModule : InjectionModule {
    override fun create() = module {
        single { PrayersPresenter(get()) }
        single { Analytics() }
    }
}