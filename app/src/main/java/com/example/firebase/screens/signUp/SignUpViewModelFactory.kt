package com.example.firebase.screens.signUp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(
    private val auth: FirebaseAuth, private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java))
            return SignUpViewModel(auth, application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}