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
public final class GetSongsUseCase_Factory implements Factory<GetSongsUseCase> {
  private final Provider<SongRepository> songRepositoryProvider;

  public GetSongsUseCase_Factory(Provider<SongRepository> songRepositoryProvider) {
    this.songRepositoryProvider = songRepositoryProvider;
  }

  @Override
  public GetSongsUseCase get() {
    return newInstance(songRepositoryProvider.get());
  }

  public static GetSongsUseCase_Factory create(Provider<SongRepository> songRepositoryProvider) {
    return new GetSongsUseCase_Factory(songRepositoryProvider);
  }

  public static GetSongsUseCase newInstance(SongRepository songRepository) {
    return new GetSongsUseCase(songRepository);
  }
}
