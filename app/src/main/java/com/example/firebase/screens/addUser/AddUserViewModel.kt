package com.example.firebase.screens.addUser

import android.app.Application
import android.os.SystemClock.sleep
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebase.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = firebaseDatabase.reference.child("MyUsers")
    private val _isloading: MutableLiveData<Boolean> = MutableLiveData()
    val isloading: LiveData<Boolean>
        get() = _isloading

    fun addUser(name: String, age: Int, email: String) {
        val id = reference.push().key.toString()
        val user = User(id, name, age, email)

        _isloading.value = true
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