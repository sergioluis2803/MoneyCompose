package com.projects.moneycompose.data.di

import android.app.Application
import androidx.room.Room
import com.projects.moneycompose.data.data_source.SpendDatabase
import com.projects.moneycompose.data.repository.SpendRepositoryImpl
import com.projects.moneycompose.domain.repository.SpendRepository
import com.projects.moneycompose.domain.use_case.DeleteAllSpend
import com.projects.moneycompose.domain.use_case.DeleteSpend
import com.projects.moneycompose.domain.use_case.GetMonthSaving
import com.projects.moneycompose.domain.use_case.GetReportPDF
import com.projects.moneycompose.domain.use_case.GetSpend
import com.projects.moneycompose.domain.use_case.InsertMonthSaving
import com.projects.moneycompose.domain.use_case.InsertReportPDF
import com.projects.moneycompose.domain.use_case.InsertSpend
import com.projects.moneycompose.domain.use_case.MoneyUseCases
import com.projects.moneycompose.domain.use_case.UpdateSpend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): SpendDatabase {
        return Room.databaseBuilder(
            application,
            SpendDatabase::class.java,
            SpendDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: SpendDatabase): SpendRepository {
        return SpendRepositoryImpl(db.dao())
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: SpendRepository): MoneyUseCases {
        return MoneyUseCases(
            getSpend = GetSpend(repository),
            insertSpend = InsertSpend(repository),
            updateSpend = UpdateSpend(repository),
            deleteSpend = DeleteSpend(repository),
            deleteAllSpend = DeleteAllSpend(repository),
            getMonthSaving = GetMonthSaving(repository),
            insertMonthSaving = InsertMonthSaving(repository),
            getReportPDF = GetReportPDF(repository),
            insertReportPDF = InsertReportPDF(repository)
        )
    }

}