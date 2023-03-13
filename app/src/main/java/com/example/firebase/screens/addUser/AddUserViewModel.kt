package com.example.firebase.screens.addUser

import android.app.Application
import android.os.SystemClock.sleep
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firebase.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddUserViewModel(firebaseDatabase: FirebaseDatabase, application: Application) :
    AndroidViewModel(application) {

    private val reference: DatabaseReference = firebaseDatabase.reference.child("MyUsers")
    private val _isloading: MutableLiveData<Boolean> = MutableLiveData()
    val isloading: LiveData<Boolean>
        get() = _isloading

    fun addUser(name: String, age: Int, email: String) {
        val id = reference.push().key.toString()
        val user = User(id, name, age, email)

        _isloading.value = true
        viewModelScope.launch {
            reference.child(id).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        getApplication(), "User Added Successfully", Toast.LENGTH_SHORT
                    ).show()
                } else Toast.makeText(
                    getApplication(), task.exception.toString(), Toast.LENGTH_SHORT
                ).show()
                _isloading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any coroutines running in this ViewModel's scope
        viewModelScope.cancel()
    }
}