package kz.tilsimsozder.message

import android.content.Context
import kz.tilsimsozder.common.InjectionModule
import kz.tilsimsozder.message.api.MessageApi
import kz.tilsimsozder.message.ui.MessagePresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MessageModule : InjectionModule {
    override fun create() = module {
        viewModel {(context: Context) ->
            MessagePresenter(context, get(), get())
        }
        single { MessageApi(get(), get()) }
    }
}