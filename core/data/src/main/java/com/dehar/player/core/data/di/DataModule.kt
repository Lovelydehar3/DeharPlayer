package com.dehar.player.core.data.di

import android.content.Context
import com.dehar.player.core.data.database.DeharDatabase
import com.dehar.player.core.data.repository.VideoRepositoryImpl
import com.dehar.player.core.data.repository.SongRepositoryImpl
import com.dehar.player.core.domain.repository.VideoRepository
import com.dehar.player.core.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDeharDatabase(
        @ApplicationContext context: Context
    ): DeharDatabase {
        return DeharDatabase.getDatabase(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindVideoRepository(impl: VideoRepositoryImpl): VideoRepository

    @Binds
    @Singleton
    abstract fun bindSongRepository(impl: SongRepositoryImpl): SongRepository
}
