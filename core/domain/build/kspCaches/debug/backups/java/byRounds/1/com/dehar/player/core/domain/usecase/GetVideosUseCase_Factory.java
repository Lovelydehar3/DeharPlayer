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
public final class GetVideosUseCase_Factory implements Factory<GetVideosUseCase> {
  private final Provider<VideoRepository> videoRepositoryProvider;

  public GetVideosUseCase_Factory(Provider<VideoRepository> videoRepositoryProvider) {
    this.videoRepositoryProvider = videoRepositoryProvider;
  }

  @Override
  public GetVideosUseCase get() {
    return newInstance(videoRepositoryProvider.get());
  }

  public static GetVideosUseCase_Factory create(Provider<VideoRepository> videoRepositoryProvider) {
    return new GetVideosUseCase_Factory(videoRepositoryProvider);
  }

  public static GetVideosUseCase newInstance(VideoRepository videoRepository) {
    return new GetVideosUseCase(videoRepository);
  }
}
