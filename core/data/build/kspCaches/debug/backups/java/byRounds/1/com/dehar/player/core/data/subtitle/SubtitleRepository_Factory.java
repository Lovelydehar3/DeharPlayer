package com.dehar.player.core.data.subtitle;

import android.content.Context;
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
public final class SubtitleRepository_Factory implements Factory<SubtitleRepository> {
  private final Provider<Context> contextProvider;

  public SubtitleRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SubtitleRepository get() {
    return newInstance(contextProvider.get());
  }

  public static SubtitleRepository_Factory create(Provider<Context> contextProvider) {
    return new SubtitleRepository_Factory(contextProvider);
  }

  public static SubtitleRepository newInstance(Context context) {
    return new SubtitleRepository(context);
  }
}
