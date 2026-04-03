package com.nyayasetu.ui.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object Home : AppDestination("home")
    data object FirForm : AppDestination("fir_form")
    data object FirUpload : AppDestination("fir_upload")
    data object FirVoice : AppDestination("fir_voice")
    data object Analysis : AppDestination("analysis")
    data object EvidenceAnalysis : AppDestination("evidence_analysis")
    data object Lawyers : AppDestination("lawyers")
    data object Feed : AppDestination("feed")
    data object Conversations : AppDestination("conversations")
    data object Chat : AppDestination("chat")
}
