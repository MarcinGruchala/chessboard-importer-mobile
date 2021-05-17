package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.chessboard_importer.repository.ApplicationRepository

class CameraModuleViewModel(application: Application) : AndroidViewModel(application) {
    private val contentResolver by lazy { application.contentResolver }

    fun createPhotoData(uri: Uri){
        Log.d("CameraModuleViewModel","Created photo data from photo")
        val  inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            ApplicationRepository.photoData.value = it.readBytes()
        }
    }

}
