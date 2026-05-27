package com.dehar.player.core.domain.usecase;

import com.dehar.player.core.domain.repository.VideoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class UpdateVideoPlaybackUseCase_Factory implements Factory<UpdateVideoPlaybackUseCase> {
  private final Provider<VideoRepository> videoRepositoryProvider;

  public UpdateVideoPlaybackUseCase_Factory(Provider<VideoRepository> videoRepositoryProvider) {
    this.videoRepositoryProvider = videoRepositoryProvider;
  }

  @Override
  public UpdateVideoPlaybackUseCase get() {
    return newInstance(videoRepositoryProvider.get());
  }

  public static UpdateVideoPlaybackUseCase_Factory create(
      Provider<VideoRepository> videoRepositoryProvider) {
    return new UpdateVideoPlaybackUseCase_Factory(videoRepositoryProvider);
  }

  public static UpdateVideoPlaybackUseCase newInstance(VideoRepository videoRepository) {
    return new UpdateVideoPlaybackUseCase(videoRepository);
  }
}
