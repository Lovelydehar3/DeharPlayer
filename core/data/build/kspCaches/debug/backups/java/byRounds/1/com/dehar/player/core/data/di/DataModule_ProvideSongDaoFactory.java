package com.dehar.player.core.data.di;

import com.dehar.player.core.data.database.DeharDatabase;
import com.dehar.player.core.data.database.SongDao;
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
public final class DataModule_ProvideSongDaoFactory implements Factory<SongDao> {
  private final Provider<DeharDatabase> databaseProvider;

  public DataModule_ProvideSongDaoFactory(Provider<DeharDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SongDao get() {
    return provideSongDao(databaseProvider.get());
  }

  public static DataModule_ProvideSongDaoFactory create(Provider<DeharDatabase> databaseProvider) {
    return new DataModule_ProvideSongDaoFactory(databaseProvider);
  }

  public static SongDao provideSongDao(DeharDatabase database) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSongDao(database));
  }
}
