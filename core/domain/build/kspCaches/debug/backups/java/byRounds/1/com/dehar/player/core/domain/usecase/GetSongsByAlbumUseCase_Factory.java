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
public final class GetSongsByAlbumUseCase_Factory implements Factory<GetSongsByAlbumUseCase> {
  private final Provider<SongRepository> songRepositoryProvider;

  public GetSongsByAlbumUseCase_Factory(Provider<SongRepository> songRepositoryProvider) {
    this.songRepositoryProvider = songRepositoryProvider;
  }

  @Override
  public GetSongsByAlbumUseCase get() {
    return newInstance(songRepositoryProvider.get());
  }

  public static GetSongsByAlbumUseCase_Factory create(
      Provider<SongRepository> songRepositoryProvider) {
    return new GetSongsByAlbumUseCase_Factory(songRepositoryProvider);
  }

  public static GetSongsByAlbumUseCase newInstance(SongRepository songRepository) {
    return new GetSongsByAlbumUseCase(songRepository);
  }
}
