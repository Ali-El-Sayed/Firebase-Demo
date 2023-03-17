package com.example.firebase.screens.addUser

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemClock.sleep
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firebase.models.User
import com.example.firebase.permissions.PhotosPermission
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddUserViewModel(
    firebaseDatabase: FirebaseDatabase,
    application: Application,
    activityResultRegistry: ActivityResultRegistry,

    ) : AndroidViewModel(application) {

    private val requestPermissionLauncher = activityResultRegistry.register(
        "requestPermission", ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) selectImage()
        else Toast.makeText(
            getApplication(), "Permission Denied", Toast.LENGTH_SHORT
        ).show()
    }
    private val photoReslutLauncher =
        activityResultRegistry.register("2", ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) _imgUri.value = it.data?.data
        }
    private val photosPermission by lazy { PhotosPermission() }
    private val reference: DatabaseReference = firebaseDatabase.reference.child("MyUsers")
    private val _isloading: MutableLiveData<Boolean> = MutableLiveData()
    val isloading: LiveData<Boolean>
        get() = _isloading

    private var _imgUri: MutableLiveData<Uri?> = MutableLiveData()
    val imgUri: LiveData<Uri?>
        get() = _imgUri


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

    fun getStoragePermission() {
        if (photosPermission.checkPermission(
                getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            selectImage()
        } else {
            val readImagePermission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
            requestPermissionLauncher.launch(readImagePermission)
        }

    }

    fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) MediaStore.ACTION_PICK_IMAGES
            else Intent.ACTION_GET_CONTENT
        }
        photoReslutLauncher.launch(intent)
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any coroutines running in this ViewModel's scope
        viewModelScope.cancel()
    }
}