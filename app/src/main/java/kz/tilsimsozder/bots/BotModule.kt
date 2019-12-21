package kz.tilsimsozder.bots

import android.content.Context
import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.bots.presenter.BotPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object BotModule : InjectionModule {
    override fun create() = module {
        single {
            val context: Context = get()
            BotPresenter(get(), context)
        }
    }
}