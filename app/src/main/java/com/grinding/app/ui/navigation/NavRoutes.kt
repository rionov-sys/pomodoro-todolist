package com.grinding.app.ui.navigation

object NavRoutes {
    const val AUTH = "auth"
    const val BERANDA = "beranda"
    const val KALENDER = "kalender"
    const val TIMER = "timer"
    const val STATISTIK = "statistik"
    const val PROFIL = "profil"
    const val TASK_ADD = "task_add"
    const val TASK_EDIT = "task_edit/{taskId}"

    fun taskEdit(taskId: String): String = "task_edit/$taskId"
}
