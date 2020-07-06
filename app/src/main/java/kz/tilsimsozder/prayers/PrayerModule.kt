package kz.tilsimsozder.prayers

import android.content.Context
import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.api.PrayersApi
import kz.tilsimsozder.prayers.presenter.PrayersPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PrayerModule : InjectionModule {
    override fun create() = module {
        viewModel {(context: Context) ->
            PrayersPresenter(context, get(), get())
        }
        single { Analytics() }
        single { PrayersApi(get(), get()) }
    }
}