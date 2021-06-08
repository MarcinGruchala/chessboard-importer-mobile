package com.example.chessboard_importer.repository

import androidx.lifecycle.MutableLiveData
import com.example.chessboard_importer.models.Board

object ApplicationRepository  {
    val photoData: MutableLiveData<ByteArray> by lazy { MutableLiveData<ByteArray>() }
    var newPhotoData = false
    var board = Board()
}
