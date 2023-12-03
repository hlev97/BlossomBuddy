package com.painandpanic.blossombuddy.di

import androidx.room.Room
import com.painandpanic.blossombuddy.data.local.database.HistoryDatabase
import com.painandpanic.blossombuddy.domain.usecase.ClassifyImageUseCase
import com.painandpanic.blossombuddy.domain.usecase.LoadHistoryUseCase
import com.painandpanic.blossombuddy.domain.usecase.LoadImageFromGalleryUseCase
import com.painandpanic.blossombuddy.domain.usecase.SavePhotoToGalleryUseCase
import com.painandpanic.blossombuddy.domain.usecase.SaveToHistoryUseCase
import com.painandpanic.blossombuddy.domain.usecase.TakePhotoUseCase
import com.painandpanic.blossombuddy.ui.camera.CameraViewModel
import com.painandpanic.blossombuddy.ui.classificationresult.ClassificationResultViewModel
import com.painandpanic.blossombuddy.ui.history.HistoryItemViewModel
import com.painandpanic.blossombuddy.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    single { TakePhotoUseCase(androidContext()) }
    single { SavePhotoToGalleryUseCase(androidContext()) }
    single { ClassifyImageUseCase(androidContext()) }
    single { LoadHistoryUseCase(get(),get()) }
    single { SaveToHistoryUseCase(get()) }
    single { LoadImageFromGalleryUseCase(androidContext(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CameraViewModel(get(),get()) }
    viewModel { ClassificationResultViewModel(get(),get(),get()) }
    viewModel { HistoryItemViewModel(get(),get()) }

}

val roomModule = module {
    single { Room.databaseBuilder(androidContext(), HistoryDatabase::class.java, "history_db").build() }
    single { get<HistoryDatabase>().historyDao() }
}