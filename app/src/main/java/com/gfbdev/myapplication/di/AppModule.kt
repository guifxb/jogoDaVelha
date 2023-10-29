package com.gfbdev.myapplication.di

import com.gfbdev.myapplication.data.PastPlaysDatabase
import com.gfbdev.myapplication.data.PastPlaysRepository
import com.gfbdev.myapplication.presentation.viewModel.GameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { GameViewModel( pastPlaysRepository = get() ) }
    single { PastPlaysRepository(get()) }
    single { PastPlaysDatabase.getDatabase(androidContext()).pastPlaysDao() }



}
