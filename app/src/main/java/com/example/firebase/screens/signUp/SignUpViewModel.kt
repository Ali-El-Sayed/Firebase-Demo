package com.example.firebase.screens.signUp

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firebase.util.validation.EmailValidator
import com.example.firebase.util.validation.PasswordValidator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val auth: FirebaseAuth, private val application: Application
) : AndroidViewModel(application) {
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    private var _status: MutableLiveData<Boolean> = MutableLiveData()
    val status: LiveData<Boolean>
        get() = _status

    fun signUpWithFirebase(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        application, "Account Created Successfully", Toast.LENGTH_SHORT
                    ).show()
                    _status.value = true
                } else Toast.makeText(application, task.exception?.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun validateEmail(email: String): String? {
        return emailValidator.validate(email)
    }

    fun validatePassword(password: String): String? {
        return passwordValidator.validate(password)
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any coroutines running in this ViewModel's scope
        viewModelScope.cancel()
    }
}