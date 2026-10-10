package com.grinding.app.service;

import com.grinding.app.data.repository.PomodoroRepository;
import com.grinding.app.data.repository.TaskRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class PomodoroManager_Factory implements Factory<PomodoroManager> {
  private final Provider<PomodoroRepository> pomodoroRepositoryProvider;

  private final Provider<TaskRepository> taskRepositoryProvider;

  public PomodoroManager_Factory(Provider<PomodoroRepository> pomodoroRepositoryProvider,
      Provider<TaskRepository> taskRepositoryProvider) {
    this.pomodoroRepositoryProvider = pomodoroRepositoryProvider;
    this.taskRepositoryProvider = taskRepositoryProvider;
  }

  @Override
  public PomodoroManager get() {
    return newInstance(pomodoroRepositoryProvider.get(), taskRepositoryProvider.get());
  }

  public static PomodoroManager_Factory create(
      Provider<PomodoroRepository> pomodoroRepositoryProvider,
      Provider<TaskRepository> taskRepositoryProvider) {
    return new PomodoroManager_Factory(pomodoroRepositoryProvider, taskRepositoryProvider);
  }

  public static PomodoroManager newInstance(PomodoroRepository pomodoroRepository,
      TaskRepository taskRepository) {
    return new PomodoroManager(pomodoroRepository, taskRepository);
  }
}
