package kz.tilsimsozder.message

import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.news.presenter.NewsPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MessageModule : InjectionModule {
    override fun create() = module {
        //viewModel { NewsPresenter(get(), get()) }
    }
}