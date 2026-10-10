package com.grinding.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.grinding.app.data.repository.AuthRepositoryImpl;
import com.grinding.app.data.repository.PomodoroRepositoryImpl;
import com.grinding.app.data.repository.TaskRepositoryImpl;
import com.grinding.app.di.NetworkModule_ProvideSupabaseAuthFactory;
import com.grinding.app.di.NetworkModule_ProvideSupabaseClientFactory;
import com.grinding.app.di.NetworkModule_ProvideSupabasePostgrestFactory;
import com.grinding.app.service.PomodoroManager;
import com.grinding.app.service.PomodoroService;
import com.grinding.app.service.PomodoroService_MembersInjector;
import com.grinding.app.ui.screens.auth.AuthViewModel;
import com.grinding.app.ui.screens.auth.AuthViewModel_HiltModules;
import com.grinding.app.ui.viewmodel.StatistikViewModel;
import com.grinding.app.ui.viewmodel.StatistikViewModel_HiltModules;
import com.grinding.app.ui.viewmodel.TaskViewModel;
import com.grinding.app.ui.viewmodel.TaskViewModel_HiltModules;
import com.grinding.app.ui.viewmodel.TimerViewModel;
import com.grinding.app.ui.viewmodel.TimerViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.auth.Auth;
import io.github.jan.supabase.postgrest.Postgrest;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerGrindingApp_HiltComponents_SingletonC {
  private DaggerGrindingApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static GrindingApp_HiltComponents.SingletonC create() {
    return new Builder().build();
  }

  public static final class Builder {
    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public GrindingApp_HiltComponents.SingletonC build() {
      return new SingletonCImpl();
    }
  }

  private static final class ActivityRetainedCBuilder implements GrindingApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements GrindingApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements GrindingApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements GrindingApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements GrindingApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements GrindingApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements GrindingApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public GrindingApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends GrindingApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends GrindingApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends GrindingApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends GrindingApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(4).put(LazyClassKeyProvider.com_grinding_app_ui_screens_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_grinding_app_ui_viewmodel_StatistikViewModel, StatistikViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_grinding_app_ui_viewmodel_TaskViewModel, TaskViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_grinding_app_ui_viewmodel_TimerViewModel, TimerViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_grinding_app_ui_screens_auth_AuthViewModel = "com.grinding.app.ui.screens.auth.AuthViewModel";

      static String com_grinding_app_ui_viewmodel_TimerViewModel = "com.grinding.app.ui.viewmodel.TimerViewModel";

      static String com_grinding_app_ui_viewmodel_TaskViewModel = "com.grinding.app.ui.viewmodel.TaskViewModel";

      static String com_grinding_app_ui_viewmodel_StatistikViewModel = "com.grinding.app.ui.viewmodel.StatistikViewModel";

      @KeepFieldType
      AuthViewModel com_grinding_app_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      TimerViewModel com_grinding_app_ui_viewmodel_TimerViewModel2;

      @KeepFieldType
      TaskViewModel com_grinding_app_ui_viewmodel_TaskViewModel2;

      @KeepFieldType
      StatistikViewModel com_grinding_app_ui_viewmodel_StatistikViewModel2;
    }
  }

  private static final class ViewModelCImpl extends GrindingApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<StatistikViewModel> statistikViewModelProvider;

    private Provider<TaskViewModel> taskViewModelProvider;

    private Provider<TimerViewModel> timerViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.statistikViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.taskViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.timerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(4).put(LazyClassKeyProvider.com_grinding_app_ui_screens_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_grinding_app_ui_viewmodel_StatistikViewModel, ((Provider) statistikViewModelProvider)).put(LazyClassKeyProvider.com_grinding_app_ui_viewmodel_TaskViewModel, ((Provider) taskViewModelProvider)).put(LazyClassKeyProvider.com_grinding_app_ui_viewmodel_TimerViewModel, ((Provider) timerViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_grinding_app_ui_viewmodel_TimerViewModel = "com.grinding.app.ui.viewmodel.TimerViewModel";

      static String com_grinding_app_ui_viewmodel_StatistikViewModel = "com.grinding.app.ui.viewmodel.StatistikViewModel";

      static String com_grinding_app_ui_viewmodel_TaskViewModel = "com.grinding.app.ui.viewmodel.TaskViewModel";

      static String com_grinding_app_ui_screens_auth_AuthViewModel = "com.grinding.app.ui.screens.auth.AuthViewModel";

      @KeepFieldType
      TimerViewModel com_grinding_app_ui_viewmodel_TimerViewModel2;

      @KeepFieldType
      StatistikViewModel com_grinding_app_ui_viewmodel_StatistikViewModel2;

      @KeepFieldType
      TaskViewModel com_grinding_app_ui_viewmodel_TaskViewModel2;

      @KeepFieldType
      AuthViewModel com_grinding_app_ui_screens_auth_AuthViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.grinding.app.ui.screens.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryImplProvider.get());

          case 1: // com.grinding.app.ui.viewmodel.StatistikViewModel 
          return (T) new StatistikViewModel(singletonCImpl.taskRepositoryImplProvider.get(), singletonCImpl.pomodoroRepositoryImplProvider.get());

          case 2: // com.grinding.app.ui.viewmodel.TaskViewModel 
          return (T) new TaskViewModel(singletonCImpl.taskRepositoryImplProvider.get());

          case 3: // com.grinding.app.ui.viewmodel.TimerViewModel 
          return (T) new TimerViewModel(singletonCImpl.pomodoroManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends GrindingApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends GrindingApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectPomodoroService(PomodoroService arg0) {
      injectPomodoroService2(arg0);
    }

    private PomodoroService injectPomodoroService2(PomodoroService instance) {
      PomodoroService_MembersInjector.injectPomodoroManager(instance, singletonCImpl.pomodoroManagerProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends GrindingApp_HiltComponents.SingletonC {
    private final SingletonCImpl singletonCImpl = this;

    private Provider<SupabaseClient> provideSupabaseClientProvider;

    private Provider<Auth> provideSupabaseAuthProvider;

    private Provider<AuthRepositoryImpl> authRepositoryImplProvider;

    private Provider<Postgrest> provideSupabasePostgrestProvider;

    private Provider<TaskRepositoryImpl> taskRepositoryImplProvider;

    private Provider<PomodoroRepositoryImpl> pomodoroRepositoryImplProvider;

    private Provider<PomodoroManager> pomodoroManagerProvider;

    private SingletonCImpl() {

      initialize();

    }

    @SuppressWarnings("unchecked")
    private void initialize() {
      this.provideSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 2));
      this.provideSupabaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<Auth>(singletonCImpl, 1));
      this.authRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepositoryImpl>(singletonCImpl, 0));
      this.provideSupabasePostgrestProvider = DoubleCheck.provider(new SwitchingProvider<Postgrest>(singletonCImpl, 4));
      this.taskRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<TaskRepositoryImpl>(singletonCImpl, 3));
      this.pomodoroRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<PomodoroRepositoryImpl>(singletonCImpl, 5));
      this.pomodoroManagerProvider = DoubleCheck.provider(new SwitchingProvider<PomodoroManager>(singletonCImpl, 6));
    }

    @Override
    public void injectGrindingApp(GrindingApp arg0) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.grinding.app.data.repository.AuthRepositoryImpl 
          return (T) new AuthRepositoryImpl(singletonCImpl.provideSupabaseAuthProvider.get());

          case 1: // io.github.jan.supabase.auth.Auth 
          return (T) NetworkModule_ProvideSupabaseAuthFactory.provideSupabaseAuth(singletonCImpl.provideSupabaseClientProvider.get());

          case 2: // io.github.jan.supabase.SupabaseClient 
          return (T) NetworkModule_ProvideSupabaseClientFactory.provideSupabaseClient();

          case 3: // com.grinding.app.data.repository.TaskRepositoryImpl 
          return (T) new TaskRepositoryImpl(singletonCImpl.provideSupabasePostgrestProvider.get(), singletonCImpl.authRepositoryImplProvider.get());

          case 4: // io.github.jan.supabase.postgrest.Postgrest 
          return (T) NetworkModule_ProvideSupabasePostgrestFactory.provideSupabasePostgrest(singletonCImpl.provideSupabaseClientProvider.get());

          case 5: // com.grinding.app.data.repository.PomodoroRepositoryImpl 
          return (T) new PomodoroRepositoryImpl(singletonCImpl.provideSupabasePostgrestProvider.get(), singletonCImpl.authRepositoryImplProvider.get());

          case 6: // com.grinding.app.service.PomodoroManager 
          return (T) new PomodoroManager(singletonCImpl.pomodoroRepositoryImplProvider.get(), singletonCImpl.taskRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
