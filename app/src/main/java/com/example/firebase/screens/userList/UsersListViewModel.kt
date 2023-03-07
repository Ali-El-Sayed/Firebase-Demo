package com.example.firebase.screens.userList

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebase.models.User
import com.google.firebase.database.*

private const val TAG: String = "UsersListViewModel"

class UsersListViewModel(application: Application) : AndroidViewModel(application) {
    private var _userList: MutableLiveData<MutableList<User>> = MutableLiveData(mutableListOf())
    val userList: LiveData<MutableList<User>>
        get() = _userList

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = firebaseDatabase.reference.child("MyUsers")

    init {
        getDataFromDatabase()
    }

    fun deleteUser(position: Int) {
        val userId = userList.value?.get(position)?.id ?: ""
        reference.child(userId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) Toast.makeText(
                getApplication(),
                "User Deleted",
                Toast.LENGTH_SHORT
            ).show()
            else Log.d(TAG, "deleteUser: ${task.exception}")
        }
    }

    private fun getDataFromDatabase() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (_userList.value?.isNotEmpty() == true) _userList.value?.clear()
                for (user in snapshot.children) user.getValue(User::class.java)?.let {
                    _userList.value?.add(it)
                    Log.d(TAG, "onDataChange: $it ")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteAllUsers() {
        reference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    getApplication(),
                    "All Users Deleted",
                    Toast.LENGTH_SHORT
                ).show()
            } else Log.d(TAG, "deleteAllUsers: ${task.exception}")
        }
    }
}