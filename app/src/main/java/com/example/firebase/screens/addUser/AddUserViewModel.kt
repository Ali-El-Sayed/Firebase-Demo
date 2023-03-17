package com.example.firebase.screens.addUser

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val TAG = "AddUserViewModel"

class AddUserViewModel(
    firebaseDatabase: FirebaseDatabase,
    firebaseStorage: FirebaseStorage,
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
    private val photoResultLauncher =
        activityResultRegistry.register("2", ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) _imgUri.value = it.data?.data
        }
    private val photosPermission by lazy { PhotosPermission() }
    private val reference: DatabaseReference = firebaseDatabase.reference.child("MyUsers")
    private val storageReference = firebaseStorage.reference
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _imgUri: MutableLiveData<Uri?> = MutableLiveData()
    val imgUri: LiveData<Uri?>
        get() = _imgUri

    private fun uploadImage(): String {
        _isLoading.value = true
        val uuid = UUID.randomUUID().toString()
        val imageRef = storageReference.child("Images").child(uuid)
        var url = ""

        CoroutineScope(Dispatchers.IO).launch {
            imgUri.value?.let { uri ->
                imageRef.putFile(uri).await()
                url = imageRef.downloadUrl.await().toString()
                Log.d(TAG, "uploadImage: $url")
            }
        }
        Log.d(TAG, "uploadImage2: $url")
        _isLoading.value = false
        return url
    }

    fun addUser(name: String, age: Int, email: String) {
        val id = reference.push().key.toString()
        val url = uploadImage()
        val user = User(id, name, age, email, url)

        _isLoading.value = true
        viewModelScope.launch {
            reference.child(id).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        getApplication(), "User Added Successfully", Toast.LENGTH_SHORT
                    ).show()
                } else Toast.makeText(
                    getApplication(), task.exception.toString(), Toast.LENGTH_SHORT
                ).show()
                _isLoading.value = false
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
                else Manifest.permission.READ_EXTERNAL_STORAGE
            requestPermissionLauncher.launch(readImagePermission)
        }

    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) MediaStore.ACTION_PICK_IMAGES
            else Intent.ACTION_GET_CONTENT
        }
        photoResultLauncher.launch(intent)
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any coroutines running in this ViewModel's scope
        viewModelScope.cancel()
    }
}