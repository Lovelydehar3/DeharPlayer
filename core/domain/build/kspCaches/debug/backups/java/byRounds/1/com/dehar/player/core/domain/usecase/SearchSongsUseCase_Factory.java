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
public final class SearchSongsUseCase_Factory implements Factory<SearchSongsUseCase> {
  private final Provider<SongRepository> songRepositoryProvider;

  public SearchSongsUseCase_Factory(Provider<SongRepository> songRepositoryProvider) {
    this.songRepositoryProvider = songRepositoryProvider;
  }

  @Override
  public SearchSongsUseCase get() {
    return newInstance(songRepositoryProvider.get());
  }

  public static SearchSongsUseCase_Factory create(Provider<SongRepository> songRepositoryProvider) {
    return new SearchSongsUseCase_Factory(songRepositoryProvider);
  }

  public static SearchSongsUseCase newInstance(SongRepository songRepository) {
    return new SearchSongsUseCase(songRepository);
  }
}
