package com.dehar.player.core.domain.usecase;

import com.dehar.player.core.domain.repository.SongRepository;
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
public final class UpdateSongPlaybackUseCase_Factory implements Factory<UpdateSongPlaybackUseCase> {
  private final Provider<SongRepository> songRepositoryProvider;

  public UpdateSongPlaybackUseCase_Factory(Provider<SongRepository> songRepositoryProvider) {
    this.songRepositoryProvider = songRepositoryProvider;
  }

  @Override
  public UpdateSongPlaybackUseCase get() {
    return newInstance(songRepositoryProvider.get());
  }

  public static UpdateSongPlaybackUseCase_Factory create(
      Provider<SongRepository> songRepositoryProvider) {
    return new UpdateSongPlaybackUseCase_Factory(songRepositoryProvider);
  }

  public static UpdateSongPlaybackUseCase newInstance(SongRepository songRepository) {
    return new UpdateSongPlaybackUseCase(songRepository);
  }
}
