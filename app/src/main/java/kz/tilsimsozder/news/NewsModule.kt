package kz.tilsimsozder.news

import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.news.presenter.NewsPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object NewsModule : InjectionModule {
    override fun create() = module {
        viewModel { NewsPresenter(get(), get()) }
    }
}