package com.nyayasetu.utils

object AppConfig {
    var isDebug: Boolean = false
        private set

    fun initialize(isDebug: Boolean) {
        this.isDebug = isDebug
    }
}
