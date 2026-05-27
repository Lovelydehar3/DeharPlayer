package com.dehar.player.core.data.repository;

import android.content.Context;
import com.dehar.player.core.data.database.SongDao;
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
public final class SongRepositoryImpl_Factory implements Factory<SongRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<SongDao> songDaoProvider;

  public SongRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<SongDao> songDaoProvider) {
    this.contextProvider = contextProvider;
    this.songDaoProvider = songDaoProvider;
  }

  @Override
  public SongRepositoryImpl get() {
    return newInstance(contextProvider.get(), songDaoProvider.get());
  }

  public static SongRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<SongDao> songDaoProvider) {
    return new SongRepositoryImpl_Factory(contextProvider, songDaoProvider);
  }

  public static SongRepositoryImpl newInstance(Context context, SongDao songDao) {
    return new SongRepositoryImpl(context, songDao);
  }
}
