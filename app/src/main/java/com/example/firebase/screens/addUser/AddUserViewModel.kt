package com.example.firebase.screens.addUser

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.firebase.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = firebaseDatabase.reference.child("MyUsers")


    fun addUser(name: String, age: Int, email: String) {
        val id = reference.push().key.toString()
        val user = User(id, name, age, email)

        reference.child(id).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) Toast.makeText(
                getApplication(), "User Added Successfully", Toast.LENGTH_SHORT
            ).show()
            else Toast.makeText(
                getApplication(), task.exception.toString(), Toast.LENGTH_SHORT
            ).show()
        }

    }
}