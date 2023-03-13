package com.example.firebase.screens.addUser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class AddUserViewModelFactory(
    private val db: FirebaseDatabase, private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddUserViewModel::class.java))
            return AddUserViewModel(db, application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}