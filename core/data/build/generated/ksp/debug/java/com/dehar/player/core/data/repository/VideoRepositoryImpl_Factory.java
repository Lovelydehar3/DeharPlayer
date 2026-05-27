package com.dehar.player.core.data.repository;

import android.content.Context;
import com.dehar.player.core.data.database.VideoDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class VideoRepositoryImpl_Factory implements Factory<VideoRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<VideoDao> videoDaoProvider;

  public VideoRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<VideoDao> videoDaoProvider) {
    this.contextProvider = contextProvider;
    this.videoDaoProvider = videoDaoProvider;
  }

  @Override
  public VideoRepositoryImpl get() {
    return newInstance(contextProvider.get(), videoDaoProvider.get());
  }

  public static VideoRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<VideoDao> videoDaoProvider) {
    return new VideoRepositoryImpl_Factory(contextProvider, videoDaoProvider);
  }

  public static VideoRepositoryImpl newInstance(Context context, VideoDao videoDao) {
    return new VideoRepositoryImpl(context, videoDao);
  }
}
