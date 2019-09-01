package kz.tilsimsozder.bots

import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.bots.presenter.BotPresenter
import org.koin.dsl.module

object BotModule : InjectionModule {
    override fun create() = module {
        single { BotPresenter(get(), get()) }
    }
}