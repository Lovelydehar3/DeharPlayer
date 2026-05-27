package com.dehar.player.core.data.di;

import com.dehar.player.core.data.database.DeharDatabase;
import com.dehar.player.core.data.database.PlaylistDao;
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
public final class DataModule_ProvidePlaylistDaoFactory implements Factory<PlaylistDao> {
  private final Provider<DeharDatabase> databaseProvider;

  public DataModule_ProvidePlaylistDaoFactory(Provider<DeharDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PlaylistDao get() {
    return providePlaylistDao(databaseProvider.get());
  }

  public static DataModule_ProvidePlaylistDaoFactory create(
      Provider<DeharDatabase> databaseProvider) {
    return new DataModule_ProvidePlaylistDaoFactory(databaseProvider);
  }

  public static PlaylistDao providePlaylistDao(DeharDatabase database) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.providePlaylistDao(database));
  }
}
