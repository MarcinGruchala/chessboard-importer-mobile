package com.example.chessboard_importer.viewmodels

import androidx.lifecycle.ViewModel
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.repository.CameraModuleRepository

class PhotoPreviewViewModel : ViewModel() {

    fun getPhotoBitmap() = CameraModuleRepository.photoBitmap

    fun notifyNewPhoto(){
        ApplicationRepository.newPhotoData = true
    }
}
