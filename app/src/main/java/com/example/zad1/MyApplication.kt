package com.example.zad1

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    // Create a CoroutineScope for database operations
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Initialize the database lazily
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
}