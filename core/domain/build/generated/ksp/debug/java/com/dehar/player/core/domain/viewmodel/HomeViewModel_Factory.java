package com.dehar.player.core.domain.viewmodel;

import com.dehar.player.core.domain.usecase.GetVideosUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetVideosUseCase> getVideosUseCaseProvider;

  public HomeViewModel_Factory(Provider<GetVideosUseCase> getVideosUseCaseProvider) {
    this.getVideosUseCaseProvider = getVideosUseCaseProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getVideosUseCaseProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<GetVideosUseCase> getVideosUseCaseProvider) {
    return new HomeViewModel_Factory(getVideosUseCaseProvider);
  }

  public static HomeViewModel newInstance(GetVideosUseCase getVideosUseCase) {
    return new HomeViewModel(getVideosUseCase);
  }
}
