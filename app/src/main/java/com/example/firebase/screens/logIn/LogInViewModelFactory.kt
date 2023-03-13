package com.example.firebase.screens.logIn

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

@Suppress("UNCHECKED_CAST")
class LogInViewModelFactory(
    private val auth: FirebaseAuth, private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogInViewModel::class.java)) return LogInViewModel(
            auth,
            application
        ) as T
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}