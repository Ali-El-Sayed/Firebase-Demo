package com.example.firebase.screens.forgetPassword

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

@Suppress("UNCHECKED_CAST")
class ForgetPasswordViewModelFactory(
    private val auth: FirebaseAuth, private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) return ForgetPasswordViewModel(
            auth,
            application
        ) as T
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}