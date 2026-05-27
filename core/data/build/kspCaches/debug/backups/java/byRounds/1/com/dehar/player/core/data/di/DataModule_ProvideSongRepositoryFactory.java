package com.dehar.player.core.data.di;

import com.dehar.player.core.data.database.SongDao;
import com.dehar.player.core.domain.repository.SongRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DataModule_ProvideSongRepositoryFactory implements Factory<SongRepository> {
  private final Provider<SongDao> songDaoProvider;

  public DataModule_ProvideSongRepositoryFactory(Provider<SongDao> songDaoProvider) {
    this.songDaoProvider = songDaoProvider;
  }

  @Override
  public SongRepository get() {
    return provideSongRepository(songDaoProvider.get());
  }

  public static DataModule_ProvideSongRepositoryFactory create(Provider<SongDao> songDaoProvider) {
    return new DataModule_ProvideSongRepositoryFactory(songDaoProvider);
  }

  public static SongRepository provideSongRepository(SongDao songDao) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSongRepository(songDao));
  }
}
