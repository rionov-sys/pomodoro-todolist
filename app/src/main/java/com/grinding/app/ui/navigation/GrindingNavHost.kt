package com.grinding.app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grinding.app.ui.components.GrindingBottomBar
import com.grinding.app.ui.screens.auth.AuthScreen
import com.grinding.app.ui.screens.auth.AuthViewModel
import com.grinding.app.ui.screens.beranda.BerandaScreen
import com.grinding.app.ui.screens.kalender.KalenderScreen
import com.grinding.app.ui.screens.profil.ProfilScreen
import com.grinding.app.ui.screens.statistik.StatistikScreen
import com.grinding.app.ui.screens.task.TaskAddEditScreen
import com.grinding.app.ui.screens.timer.TimerScreen
import com.grinding.app.ui.viewmodel.TaskViewModel

@Composable
fun GrindingNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel(),
    timerViewModel: com.grinding.app.ui.viewmodel.TimerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val authUiState by authViewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Handle auto-login and session logout navigation
    LaunchedEffect(authUiState.isAuthenticated) {
        if (authUiState.isAuthenticated) {
            if (currentRoute == NavRoutes.AUTH) {
                navController.navigate(NavRoutes.BERANDA) {
                    popUpTo(NavRoutes.AUTH) { inclusive = true }
                }
            }
        } else {
            if (currentRoute != null && currentRoute != NavRoutes.AUTH) {
                navController.navigate(NavRoutes.AUTH) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    val showBottomBar = currentRoute in listOf(
        NavRoutes.BERANDA,
        NavRoutes.KALENDER,
        NavRoutes.TIMER,
        NavRoutes.STATISTIK,
        NavRoutes.PROFIL
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                GrindingBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(NavRoutes.BERANDA) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (authUiState.isAuthenticated) NavRoutes.BERANDA else NavRoutes.AUTH,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(NavRoutes.AUTH) {
                AuthScreen(
                    onAuthSuccess = {
                        navController.navigate(NavRoutes.BERANDA) {
                            popUpTo(NavRoutes.AUTH) { inclusive = true }
                        }
                    },
                    viewModel = authViewModel
                )
            }

            composable(NavRoutes.BERANDA) {
                BerandaScreen(
                    userName = authUiState.currentUserName,
                    onAddTaskClick = {
                        navController.navigate(NavRoutes.TASK_ADD)
                    },
                    onEditTaskClick = { taskId ->
                        navController.navigate(NavRoutes.taskEdit(taskId))
                    },
                    onStartPomodoroForTask = { taskId, taskTitle ->
                        timerViewModel.setTask(taskId, taskTitle)
                        navController.navigate(NavRoutes.TIMER)
                    },
                    onQuickSprintClick = {
                        timerViewModel.setTask(null, "Sprint Kilat")
                        navController.navigate(NavRoutes.TIMER)
                    },
                    taskViewModel = taskViewModel
                )
            }

            composable(NavRoutes.KALENDER) {
                KalenderScreen(
                    onTaskClick = { taskId ->
                        navController.navigate(NavRoutes.taskEdit(taskId))
                    },
                    taskViewModel = taskViewModel
                )
            }

            composable(NavRoutes.TIMER) {
                TimerScreen(
                    timerViewModel = timerViewModel
                )
            }

            composable(NavRoutes.STATISTIK) {
                StatistikScreen()
            }

            composable(NavRoutes.PROFIL) {
                ProfilScreen(
                    onLogoutClick = {
                        navController.navigate(NavRoutes.AUTH) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    authViewModel = authViewModel
                )
            }

            composable(NavRoutes.TASK_ADD) {
                TaskAddEditScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSaveTask = { title, est, date, color, start, end ->
                        taskViewModel.createTask(
                            title = title,
                            estimatedPomodoros = est,
                            taskDate = date,
                            color = color,
                            startTime = start,
                            endTime = end
                        )
                    }
                )
            }

            composable(
                route = NavRoutes.TASK_EDIT,
                arguments = listOf(
                    navArgument("taskId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                val existingTask = taskId?.let { taskViewModel.getTaskById(it) }
                TaskAddEditScreen(
                    taskId = taskId,
                    existingTask = existingTask,
                    onNavigateBack = { navController.popBackStack() },
                    onSaveTask = { title, est, date, color, start, end ->
                        taskViewModel.createTask(
                            title = title,
                            estimatedPomodoros = est,
                            taskDate = date,
                            color = color,
                            startTime = start,
                            endTime = end
                        )
                    },
                    onUpdateTask = { updatedTask ->
                        taskViewModel.updateTask(updatedTask)
                    },
                    onDeleteTask = { id ->
                        taskViewModel.deleteTask(id)
                    }
                )
            }
        }
    }
}
