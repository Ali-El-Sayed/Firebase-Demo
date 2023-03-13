package com.example.firebase.screens.updaterUser

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firebase.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel

private const val TAG = "UpdateUserViewmodel"

class UpdateUserViewmodel(db: FirebaseDatabase, application: Application) :
    AndroidViewModel(application) {
    private val _status: MutableLiveData<String> = MutableLiveData()
    private val status: MutableLiveData<String>
        get() = _status

    private val _isloading: MutableLiveData<Boolean> = MutableLiveData()
    val isloading: LiveData<Boolean>
        get() = _isloading

    var updatedUser: MutableLiveData<User> = MutableLiveData()

    private val reference: DatabaseReference = db.reference.child("MyUsers")

    fun updateUser() {
        val userMap = mutableMapOf<String, Any>()
        userMap["id"] = this.updatedUser.value?.id?.trim() ?: ""
        userMap["name"] = updatedUser.value?.name?.trim() ?: ""
        userMap["email"] = updatedUser.value?.email?.trim() ?: ""
        userMap["age"] = updatedUser.value?.age ?: 0

        _isloading.value = true
        reference.child(updatedUser.value?.id ?: "").updateChildren(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    status.value = "User Updated Successfully "
                    Toast.makeText(
                        getApplication(), "User Updated", Toast.LENGTH_SHORT
                    ).show()
                } else Log.d(TAG, "updateUser: ${task.exception}")
                _isloading.value = false
            }
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any coroutines running in this ViewModel's scope
        viewModelScope.cancel()
    }
}