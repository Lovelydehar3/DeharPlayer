package com.dehar.player.core.data.di;

import android.content.Context;
import com.dehar.player.core.data.database.DeharDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataModule_ProvideDeharDatabaseFactory implements Factory<DeharDatabase> {
  private final Provider<Context> contextProvider;

  public DataModule_ProvideDeharDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DeharDatabase get() {
    return provideDeharDatabase(contextProvider.get());
  }

  public static DataModule_ProvideDeharDatabaseFactory create(Provider<Context> contextProvider) {
    return new DataModule_ProvideDeharDatabaseFactory(contextProvider);
  }

  public static DeharDatabase provideDeharDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideDeharDatabase(context));
  }
}
