package kz.tilsimsozder.preference

import kz.tilsimsozder.common.InjectionModule
import org.koin.dsl.bind
import org.koin.dsl.module

object PreferenceModule : InjectionModule {
    override fun create() = module {
        single { SharedPreference(get()) } bind PreferenceContract::class
    }
}