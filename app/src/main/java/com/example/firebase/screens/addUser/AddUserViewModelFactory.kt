package com.example.firebase.screens.addUser

import android.app.Application
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class AddUserViewModelFactory(
    private val db: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val application: Application,
    private val activityResultRegistry: ActivityResultRegistry
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddUserViewModel::class.java)) return AddUserViewModel(
            db, firebaseStorage, application, activityResultRegistry
        ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}