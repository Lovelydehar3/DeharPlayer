package com.dehar.player.core.data.di;

import com.dehar.player.core.data.database.DeharDatabase;
import com.dehar.player.core.data.database.VideoDao;
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
public final class DataModule_ProvideVideoDaoFactory implements Factory<VideoDao> {
  private final Provider<DeharDatabase> databaseProvider;

  public DataModule_ProvideVideoDaoFactory(Provider<DeharDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public VideoDao get() {
    return provideVideoDao(databaseProvider.get());
  }

  public static DataModule_ProvideVideoDaoFactory create(Provider<DeharDatabase> databaseProvider) {
    return new DataModule_ProvideVideoDaoFactory(databaseProvider);
  }

  public static VideoDao provideVideoDao(DeharDatabase database) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideVideoDao(database));
  }
}
