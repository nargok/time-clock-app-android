package com.example.timeclock.config

import android.app.Application
import androidx.room.Room
import com.example.timeclock.data.db.dao.EffortDao
import com.example.timeclock.data.db.dao.StandardWorkingHourDao
import com.example.timeclock.domain.repository.EffortRepository
import com.example.timeclock.domain.repository.StandardWorkingHourRepository
import com.example.timeclock.infrastructure.repository.EffortRepositoryImpl
import com.example.timeclock.infrastructure.repository.StandardWorkingHourRepositoryImpl
import com.nargok.timeclock.domain.service.EffortService
import com.nargok.timeclock.infrastructure.service.EffortServiceImpl
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
    fun provideTimeClockDatabase(app: Application): TimeClockDatabase {
        return Room.databaseBuilder(
            app,
            TimeClockDatabase::class.java,
            TimeClockDatabase.DATABASE_NAME
        ).addMigrations(TimeClockDatabase.MIGRATION_1_2)
            .addMigrations(TimeClockDatabase.MIGRATION_2_3)
            .addMigrations(TimeClockDatabase.MIGRATION_3_4)
            .addMigrations(TimeClockDatabase.MIGRATION_4_5)
            .build()
    }

    @Provides
    @Singleton
    fun provideEffortDao(db: TimeClockDatabase): EffortDao {
        return db.effortDao();
    }

    @Provides
    @Singleton
    fun provideEffortRepository(effortDao: EffortDao): EffortRepository {
        return EffortRepositoryImpl(effortDao)
    }

    @Provides
    @Singleton
    fun provideStandardWorkingHourDao(db: TimeClockDatabase): StandardWorkingHourDao {
        return db.standardWorkingHourDao()
    }

    @Provides
    @Singleton
    fun provideStandardWorkingRepository(standardWorkingHourDao: StandardWorkingHourDao): StandardWorkingHourRepository {
        return StandardWorkingHourRepositoryImpl(standardWorkingHourDao)
    }

    @Provides
    @Singleton
    fun provideEffortService(effortRepository: EffortRepository): EffortService {
        return EffortServiceImpl(effortRepository)
    }
}
