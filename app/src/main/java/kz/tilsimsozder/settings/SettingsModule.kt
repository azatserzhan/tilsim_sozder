package kz.tilsimsozder.settings

import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.settings.presenter.SettingsPresenter
import org.koin.dsl.module

object SettingsModule : InjectionModule {
    override fun create() = module {
        single { SettingsPresenter(get()) }
    }
}