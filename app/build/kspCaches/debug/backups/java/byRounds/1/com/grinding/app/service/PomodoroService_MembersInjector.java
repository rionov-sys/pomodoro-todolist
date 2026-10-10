package com.grinding.app.service;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PomodoroService_MembersInjector implements MembersInjector<PomodoroService> {
  private final Provider<PomodoroManager> pomodoroManagerProvider;

  public PomodoroService_MembersInjector(Provider<PomodoroManager> pomodoroManagerProvider) {
    this.pomodoroManagerProvider = pomodoroManagerProvider;
  }

  public static MembersInjector<PomodoroService> create(
      Provider<PomodoroManager> pomodoroManagerProvider) {
    return new PomodoroService_MembersInjector(pomodoroManagerProvider);
  }

  @Override
  public void injectMembers(PomodoroService instance) {
    injectPomodoroManager(instance, pomodoroManagerProvider.get());
  }

  @InjectedFieldSignature("com.grinding.app.service.PomodoroService.pomodoroManager")
  public static void injectPomodoroManager(PomodoroService instance,
      PomodoroManager pomodoroManager) {
    instance.pomodoroManager = pomodoroManager;
  }
}
