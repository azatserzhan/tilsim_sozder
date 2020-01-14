package kz.tilsimsozder.tilsim

import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.tilsim.presenter.TilsimPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object TilsimModule : InjectionModule {
    override fun create() = module {
        viewModel { TilsimPresenter(get()) }
    }
}