package com.gfbdev.jogoDaVelha.di

import com.gfbdev.jogoDaVelha.data.PastPlaysDatabase
import com.gfbdev.jogoDaVelha.data.PastPlaysRepository
import com.gfbdev.jogoDaVelha.presentation.viewModel.GameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { GameViewModel( pastPlaysRepository = get() ) }
    single { PastPlaysRepository(get()) }
    single { PastPlaysDatabase.getDatabase(androidContext()).pastPlaysDao() }



}
