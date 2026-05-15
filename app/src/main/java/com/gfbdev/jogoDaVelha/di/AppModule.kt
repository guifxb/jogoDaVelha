package com.gfbdev.jogoDaVelha.di

import com.gfbdev.jogoDaVelha.data.PastPlaysDatabase
import com.gfbdev.jogoDaVelha.data.PastPlaysRepository
import com.gfbdev.jogoDaVelha.presentation.viewModel.GameViewModel
import com.gfbdev.jogoDaVelha.domain.usecase.GetCpuMoveUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // UseCases
    factory { GetCpuMoveUseCase() }

    // Repository & Database
    single { PastPlaysRepository(get()) }
    single { PastPlaysDatabase.getDatabase(androidContext()).pastPlaysDao() }

    // ViewModel
    viewModel { GameViewModel(pastPlaysRepository = get(), getCpuMoveUseCase = get()) }
}