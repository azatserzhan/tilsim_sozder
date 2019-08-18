package kz.tilsimsozder.bots

import kz.azatserzhanov.test.common.InjectionModule
import kz.tilsimsozder.bots.presenter.BotPresenter
import org.koin.dsl.module

object BotModule : InjectionModule {
    override fun create() = module {
        single { BotPresenter(get(), get()) }
    }
}