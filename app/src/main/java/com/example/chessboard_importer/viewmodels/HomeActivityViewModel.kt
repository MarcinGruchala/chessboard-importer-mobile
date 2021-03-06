package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.webservices.WebServices

import com.example.chessboard_importer.webservices.WebServicesErrorStatus

class HomeActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val contentResolver by lazy { application.contentResolver }
    private val applicationContext by lazy { application.applicationContext }
    val importedImageUri: MutableLiveData<Uri?> by lazy { MutableLiveData<Uri?>() }
    val apiConnectionErrorStatus: MutableLiveData<WebServicesErrorStatus> by lazy {
        MutableLiveData<WebServicesErrorStatus>(WebServicesErrorStatus.NOERROR)
    }

    private val apiConnectionErrorStatusObserver = Observer<WebServicesErrorStatus> { errorStatus ->
        apiConnectionErrorStatus.value = errorStatus
    }

    init {
        WebServices.webServiceErrorStatus.observeForever(apiConnectionErrorStatusObserver)
    }

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
