package com.grinding.app.di

import com.grinding.app.data.repository.AuthRepository
import com.grinding.app.data.repository.AuthRepositoryImpl
import com.grinding.app.data.repository.PomodoroRepository
import com.grinding.app.data.repository.PomodoroRepositoryImpl
import com.grinding.app.data.repository.TaskRepository
import com.grinding.app.data.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindPomodoroRepository(
        impl: PomodoroRepositoryImpl
    ): PomodoroRepository
}
