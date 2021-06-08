package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.webservices.WebServices

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
        ApplicationRepository.newPhotoData = true
    }

    fun getAccessToken(){
        WebServices.getAccessToken(applicationContext)
    }
}
