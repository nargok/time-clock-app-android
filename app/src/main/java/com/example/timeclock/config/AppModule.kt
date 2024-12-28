package com.example.timeclock.config

import android.app.Application
import androidx.room.Room
import com.example.timeclock.data.db.dao.EffortDao
import com.example.timeclock.domain.repository.EffortRepository
import com.example.timeclock.infrastructure.repository.EffortRepositoryImpl
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
}
