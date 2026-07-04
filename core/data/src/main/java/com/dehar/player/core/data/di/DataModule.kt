package com.dehar.player.core.data.di

import android.content.Context
import com.dehar.player.core.data.database.BookmarkDao
import com.dehar.player.core.data.database.DeharDatabase
import com.dehar.player.core.data.database.DownloadDao
import com.dehar.player.core.data.database.EqPresetDao
import com.dehar.player.core.data.database.NetworkStreamDao
import com.dehar.player.core.data.database.PlaybackHistoryDao
import com.dehar.player.core.data.database.PlaylistDao
import com.dehar.player.core.data.database.PrivateVaultDao
import com.dehar.player.core.data.database.RecycleBinDao
import com.dehar.player.core.data.database.SongDao
import com.dehar.player.core.data.database.SubtitleDao
import com.dehar.player.core.data.database.VideoDao
import com.dehar.player.core.data.repository.SongRepositoryImpl
import com.dehar.player.core.data.repository.VideoRepositoryImpl
import com.dehar.player.core.domain.repository.SongRepository
import com.dehar.player.core.domain.repository.VideoRepository
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
    ): DeharDatabase = DeharDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideVideoDao(db: DeharDatabase): VideoDao = db.videoDao()

    @Provides
    @Singleton
    fun provideSongDao(db: DeharDatabase): SongDao = db.songDao()

    @Provides
    @Singleton
    fun providePlaylistDao(db: DeharDatabase): PlaylistDao = db.playlistDao()

    @Provides
    @Singleton
    fun provideBookmarkDao(db: DeharDatabase): BookmarkDao = db.bookmarkDao()

    @Provides
    @Singleton
    fun provideSubtitleDao(db: DeharDatabase): SubtitleDao = db.subtitleDao()

    @Provides
    @Singleton
    fun provideHistoryDao(db: DeharDatabase): PlaybackHistoryDao = db.historyDao()

    @Provides
    @Singleton
    fun provideVaultDao(db: DeharDatabase): PrivateVaultDao = db.vaultDao()

    @Provides
    @Singleton
    fun provideDownloadDao(db: DeharDatabase): DownloadDao = db.downloadDao()

    @Provides
    @Singleton
    fun provideStreamDao(db: DeharDatabase): NetworkStreamDao = db.streamDao()

    @Provides
    @Singleton
    fun provideEqPresetDao(db: DeharDatabase): EqPresetDao = db.eqPresetDao()

    @Provides
    @Singleton
    fun provideRecycleBinDao(db: DeharDatabase): RecycleBinDao = db.recycleBinDao()
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
