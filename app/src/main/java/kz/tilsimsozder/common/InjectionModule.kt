package kz.tilsimsozder.common

import org.koin.core.module.Module

interface InjectionModule {
    fun create(): Module
}