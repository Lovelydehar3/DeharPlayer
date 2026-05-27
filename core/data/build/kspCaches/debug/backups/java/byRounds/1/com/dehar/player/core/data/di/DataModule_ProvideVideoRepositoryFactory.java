package com.dehar.player.core.data.di;

import com.dehar.player.core.data.database.VideoDao;
import com.dehar.player.core.domain.repository.VideoRepository;
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
public final class DataModule_ProvideVideoRepositoryFactory implements Factory<VideoRepository> {
  private final Provider<VideoDao> videoDaoProvider;

  public DataModule_ProvideVideoRepositoryFactory(Provider<VideoDao> videoDaoProvider) {
    this.videoDaoProvider = videoDaoProvider;
  }

  @Override
  public VideoRepository get() {
    return provideVideoRepository(videoDaoProvider.get());
  }

  public static DataModule_ProvideVideoRepositoryFactory create(
      Provider<VideoDao> videoDaoProvider) {
    return new DataModule_ProvideVideoRepositoryFactory(videoDaoProvider);
  }

  public static VideoRepository provideVideoRepository(VideoDao videoDao) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideVideoRepository(videoDao));
  }
}
