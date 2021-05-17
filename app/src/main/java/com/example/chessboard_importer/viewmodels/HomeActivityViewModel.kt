package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.chessboard_importer.repository.ApplicationRepository

class HomeActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val contentResolver by lazy { application.contentResolver }
    private val applicationContext by lazy { application.applicationContext }
    val importedImageUri: MutableLiveData<Uri?> by lazy { MutableLiveData<Uri?>() }

    fun createPhotoData(uri: Uri){
        Log.d("HomeActivityViewModel","Created photo data from import")
        val  inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            ApplicationRepository.photoData.value = it.readBytes()
        }
    }

    fun getAccessToken(){
        ApplicationRepository.getAccessToken(applicationContext)
    }
}
