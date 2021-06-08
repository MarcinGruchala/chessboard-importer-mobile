package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import com.example.chessboard_importer.models.Image
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.repository.CameraModuleRepository
import java.io.File

class CameraActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val contentResolver by lazy { application.contentResolver }
    private val applicationContext by lazy { application.applicationContext }

    fun getPhotoFile(fileName: String): File = File.createTempFile(
        fileName,
        ".jpg",
        applicationContext.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
    )

    fun createPhotoData(uri: Uri){
        val  inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            ApplicationRepository.photoData.value = it.readBytes()
        }
    }

    fun updatePhotoBitmap(bitmap: Bitmap, photoRotation: Int) {
        when(photoRotation){
            90 -> { CameraModuleRepository.photoBitmap = Image.rotateImage(bitmap,90f) }
            180 -> { CameraModuleRepository.photoBitmap = Image.rotateImage(bitmap,180f) }
            270 -> { CameraModuleRepository.photoBitmap= Image.rotateImage(bitmap,270f) }
            else -> { CameraModuleRepository.photoBitmap = bitmap }
        }
    }
}
