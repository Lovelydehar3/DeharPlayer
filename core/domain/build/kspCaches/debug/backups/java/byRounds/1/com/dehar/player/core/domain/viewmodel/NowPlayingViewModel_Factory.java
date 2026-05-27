package com.dehar.player.core.domain.viewmodel;

import com.dehar.player.core.domain.usecase.UpdateSongPlaybackUseCase;
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
public final class NowPlayingViewModel_Factory implements Factory<NowPlayingViewModel> {
  private final Provider<UpdateSongPlaybackUseCase> updateSongPlaybackUseCaseProvider;

  public NowPlayingViewModel_Factory(
      Provider<UpdateSongPlaybackUseCase> updateSongPlaybackUseCaseProvider) {
    this.updateSongPlaybackUseCaseProvider = updateSongPlaybackUseCaseProvider;
  }

  @Override
  public NowPlayingViewModel get() {
    return newInstance(updateSongPlaybackUseCaseProvider.get());
  }

  public static NowPlayingViewModel_Factory create(
      Provider<UpdateSongPlaybackUseCase> updateSongPlaybackUseCaseProvider) {
    return new NowPlayingViewModel_Factory(updateSongPlaybackUseCaseProvider);
  }

  public static NowPlayingViewModel newInstance(
      UpdateSongPlaybackUseCase updateSongPlaybackUseCase) {
    return new NowPlayingViewModel(updateSongPlaybackUseCase);
  }
}
