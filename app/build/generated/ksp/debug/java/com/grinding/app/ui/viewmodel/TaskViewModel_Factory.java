package com.grinding.app.ui.viewmodel;

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
public final class TaskViewModel_Factory implements Factory<TaskViewModel> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  public TaskViewModel_Factory(Provider<TaskRepository> taskRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
  }

  @Override
  public TaskViewModel get() {
    return newInstance(taskRepositoryProvider.get());
  }

  public static TaskViewModel_Factory create(Provider<TaskRepository> taskRepositoryProvider) {
    return new TaskViewModel_Factory(taskRepositoryProvider);
  }

  public static TaskViewModel newInstance(TaskRepository taskRepository) {
    return new TaskViewModel(taskRepository);
  }
}
