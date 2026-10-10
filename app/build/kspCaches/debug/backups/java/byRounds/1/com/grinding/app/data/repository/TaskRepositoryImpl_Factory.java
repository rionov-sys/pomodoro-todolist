package com.grinding.app.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.postgrest.Postgrest;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TaskRepositoryImpl_Factory implements Factory<TaskRepositoryImpl> {
  private final Provider<Postgrest> postgrestProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public TaskRepositoryImpl_Factory(Provider<Postgrest> postgrestProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.postgrestProvider = postgrestProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public TaskRepositoryImpl get() {
    return newInstance(postgrestProvider.get(), authRepositoryProvider.get());
  }

  public static TaskRepositoryImpl_Factory create(Provider<Postgrest> postgrestProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new TaskRepositoryImpl_Factory(postgrestProvider, authRepositoryProvider);
  }

  public static TaskRepositoryImpl newInstance(Postgrest postgrest, AuthRepository authRepository) {
    return new TaskRepositoryImpl(postgrest, authRepository);
  }
}
