package com.grinding.app.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.auth.Auth;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<Auth> authProvider;

  public AuthRepositoryImpl_Factory(Provider<Auth> authProvider) {
    this.authProvider = authProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(authProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<Auth> authProvider) {
    return new AuthRepositoryImpl_Factory(authProvider);
  }

  public static AuthRepositoryImpl newInstance(Auth auth) {
    return new AuthRepositoryImpl(auth);
  }
}
