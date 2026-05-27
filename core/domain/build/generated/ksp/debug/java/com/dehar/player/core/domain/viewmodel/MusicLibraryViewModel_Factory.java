package com.dehar.player.core.domain.viewmodel;

import com.dehar.player.core.domain.usecase.GetFavoriteSongsUseCase;
import com.dehar.player.core.domain.usecase.GetSongsUseCase;
import com.dehar.player.core.domain.usecase.SearchSongsUseCase;
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
public final class MusicLibraryViewModel_Factory implements Factory<MusicLibraryViewModel> {
  private final Provider<GetSongsUseCase> getSongsUseCaseProvider;

  private final Provider<GetFavoriteSongsUseCase> getFavoriteSongsUseCaseProvider;

  private final Provider<SearchSongsUseCase> searchSongsUseCaseProvider;

  public MusicLibraryViewModel_Factory(Provider<GetSongsUseCase> getSongsUseCaseProvider,
      Provider<GetFavoriteSongsUseCase> getFavoriteSongsUseCaseProvider,
      Provider<SearchSongsUseCase> searchSongsUseCaseProvider) {
    this.getSongsUseCaseProvider = getSongsUseCaseProvider;
    this.getFavoriteSongsUseCaseProvider = getFavoriteSongsUseCaseProvider;
    this.searchSongsUseCaseProvider = searchSongsUseCaseProvider;
  }

  @Override
  public MusicLibraryViewModel get() {
    return newInstance(getSongsUseCaseProvider.get(), getFavoriteSongsUseCaseProvider.get(), searchSongsUseCaseProvider.get());
  }

  public static MusicLibraryViewModel_Factory create(
      Provider<GetSongsUseCase> getSongsUseCaseProvider,
      Provider<GetFavoriteSongsUseCase> getFavoriteSongsUseCaseProvider,
      Provider<SearchSongsUseCase> searchSongsUseCaseProvider) {
    return new MusicLibraryViewModel_Factory(getSongsUseCaseProvider, getFavoriteSongsUseCaseProvider, searchSongsUseCaseProvider);
  }

  public static MusicLibraryViewModel newInstance(GetSongsUseCase getSongsUseCase,
      GetFavoriteSongsUseCase getFavoriteSongsUseCase, SearchSongsUseCase searchSongsUseCase) {
    return new MusicLibraryViewModel(getSongsUseCase, getFavoriteSongsUseCase, searchSongsUseCase);
  }
}
