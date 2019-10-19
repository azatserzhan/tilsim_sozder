package kz.tilsimsozder.prayers

import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.presenter.PrayersPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PrayerModule : InjectionModule {
    override fun create() = module {
        viewModel { PrayersPresenter(get(), get(), get()) }
        single { Analytics() }
    }
}