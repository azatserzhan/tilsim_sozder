package kz.tilsimsozder.inappupdates

import kz.tilsimsozder.common.InjectionModule
import org.koin.dsl.module

object InAppUpdatesModule : InjectionModule {

    override fun create() = module {
        single { InAppUpdateManager(get()) }
    }
}
