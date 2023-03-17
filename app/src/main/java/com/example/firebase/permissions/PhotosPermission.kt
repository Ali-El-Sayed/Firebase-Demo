package com.example.firebase.permissions

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PhotosPermission {
    fun checkPermission(application: Application, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            application, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}