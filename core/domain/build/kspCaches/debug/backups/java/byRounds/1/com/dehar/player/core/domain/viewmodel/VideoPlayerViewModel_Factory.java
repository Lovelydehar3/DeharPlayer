package com.dehar.player.core.domain.viewmodel;

import com.dehar.player.core.domain.usecase.UpdateVideoPlaybackUseCase;
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
public final class VideoPlayerViewModel_Factory implements Factory<VideoPlayerViewModel> {
  private final Provider<UpdateVideoPlaybackUseCase> updateVideoPlaybackUseCaseProvider;

  public VideoPlayerViewModel_Factory(
      Provider<UpdateVideoPlaybackUseCase> updateVideoPlaybackUseCaseProvider) {
    this.updateVideoPlaybackUseCaseProvider = updateVideoPlaybackUseCaseProvider;
  }

  @Override
  public VideoPlayerViewModel get() {
    return newInstance(updateVideoPlaybackUseCaseProvider.get());
  }

  public static VideoPlayerViewModel_Factory create(
      Provider<UpdateVideoPlaybackUseCase> updateVideoPlaybackUseCaseProvider) {
    return new VideoPlayerViewModel_Factory(updateVideoPlaybackUseCaseProvider);
  }

  public static VideoPlayerViewModel newInstance(
      UpdateVideoPlaybackUseCase updateVideoPlaybackUseCase) {
    return new VideoPlayerViewModel(updateVideoPlaybackUseCase);
  }
}
