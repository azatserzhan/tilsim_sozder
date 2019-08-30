package kz.tilsimsozder.news

import kz.azatserzhanov.test.common.InjectionModule
import kz.tilsimsozder.news.presenter.NewsPresenter
import org.koin.dsl.module

object NewsModule : InjectionModule {
    override fun create() = module {
        single { NewsPresenter(get(), get()) }
    }
}