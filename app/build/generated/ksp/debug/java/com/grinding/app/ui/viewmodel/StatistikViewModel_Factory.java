package com.grinding.app.ui.viewmodel;

import com.grinding.app.data.repository.PomodoroRepository;
import com.grinding.app.data.repository.TaskRepository;
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
public final class StatistikViewModel_Factory implements Factory<StatistikViewModel> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  private final Provider<PomodoroRepository> pomodoroRepositoryProvider;

  public StatistikViewModel_Factory(Provider<TaskRepository> taskRepositoryProvider,
      Provider<PomodoroRepository> pomodoroRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.pomodoroRepositoryProvider = pomodoroRepositoryProvider;
  }

  @Override
  public StatistikViewModel get() {
    return newInstance(taskRepositoryProvider.get(), pomodoroRepositoryProvider.get());
  }

  public static StatistikViewModel_Factory create(Provider<TaskRepository> taskRepositoryProvider,
      Provider<PomodoroRepository> pomodoroRepositoryProvider) {
    return new StatistikViewModel_Factory(taskRepositoryProvider, pomodoroRepositoryProvider);
  }

  public static StatistikViewModel newInstance(TaskRepository taskRepository,
      PomodoroRepository pomodoroRepository) {
    return new StatistikViewModel(taskRepository, pomodoroRepository);
  }
}
