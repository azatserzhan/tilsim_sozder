package kz.azatserzhanov.test.common

import org.koin.core.module.Module

interface InjectionModule {
    fun create(): Module
}