package com.rolandmcdoland.videojournalapp.form.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.rolandmcdoland.videojournalapp.form.FormViewModel

val formModule = module {
    viewModelOf(::FormViewModel)
}