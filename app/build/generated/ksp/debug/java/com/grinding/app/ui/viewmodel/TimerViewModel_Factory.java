package com.grinding.app.ui.viewmodel;

import com.grinding.app.service.PomodoroManager;
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
public final class TimerViewModel_Factory implements Factory<TimerViewModel> {
  private final Provider<PomodoroManager> pomodoroManagerProvider;

  public TimerViewModel_Factory(Provider<PomodoroManager> pomodoroManagerProvider) {
    this.pomodoroManagerProvider = pomodoroManagerProvider;
  }

  @Override
  public TimerViewModel get() {
    return newInstance(pomodoroManagerProvider.get());
  }

  public static TimerViewModel_Factory create(Provider<PomodoroManager> pomodoroManagerProvider) {
    return new TimerViewModel_Factory(pomodoroManagerProvider);
  }

  public static TimerViewModel newInstance(PomodoroManager pomodoroManager) {
    return new TimerViewModel(pomodoroManager);
  }
}
