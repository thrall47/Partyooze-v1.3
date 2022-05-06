package com.fenrir4.partyooze.di

import com.fenrir4.partyooze.repository.MainRepository
import com.fenrir4.partyooze.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single { MainRepository(get()) }
    viewModel { MainViewModel(get()) }

}
