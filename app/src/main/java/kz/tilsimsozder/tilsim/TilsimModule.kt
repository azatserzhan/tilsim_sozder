package kz.tilsimsozder.tilsim

import kz.azatserzhanov.test.common.InjectionModule
import kz.tilsimsozder.tilsim.presenter.TilsimPresenter
import org.koin.dsl.module

object TilsimModule : InjectionModule {
    override fun create() = module {
        single { TilsimPresenter(get(), get()) }
    }
}