package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.*
import com.example.chessboard_importer.models.ChessPlayer
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.webservices.WebServices
import com.example.chessboard_importer.webservices.WebServicesErrorStatus

class ChessboardActivityViewModel(application: Application) : AndroidViewModel(application){
    private val applicationContext by lazy { application.applicationContext }
    val newFen: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val isImageUploading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val imageUploadErrorStatus: MutableLiveData<WebServicesErrorStatus> by lazy {
        MutableLiveData<WebServicesErrorStatus>(WebServicesErrorStatus.NOERROR)
    }
    var playerChoice = ChessPlayer.WHITE

    private val postImageResponseObserver = Observer<String> { response ->

        if (isImageUploading.value!!){
            newFen.value = response
            isImageUploading.value = false
        }
    }

    private val webServiceErrorStatusObserver = Observer<WebServicesErrorStatus> { errorStatus ->
        imageUploadErrorStatus.value = errorStatus
    }

    init {
        WebServices.postImageResponse.observeForever(postImageResponseObserver)
        WebServices.webServiceErrorStatus.observeForever(webServiceErrorStatusObserver)
    }

    fun updateBoardFen(fen: String) {
        ApplicationRepository.board.fen = fen
    }

    fun getBoardFen() = ApplicationRepository.board.fen

    fun copyFen(){
        val clipBoard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Fen", getBoardFen())
        clipBoard.setPrimaryClip(clip)
    }

    fun setPlayerTurn(){
        ApplicationRepository.board.setPlayerTurn(playerChoice)
    }

    fun checkIfNewPhotoData(){
        if (WebServices.webServiceErrorStatus.value == WebServicesErrorStatus.CONNECTIONERROR){
            imageUploadErrorStatus.value = WebServicesErrorStatus.CONNECTIONERROR
            return
        }
        else{
            if (ApplicationRepository.newPhotoData){
                imageUploadErrorStatus.value=WebServicesErrorStatus.NOERROR
                WebServices.postImage(applicationContext)
                isImageUploading.value = true
                ApplicationRepository.newPhotoData = false
            }
            else{
                imageUploadErrorStatus.value=WebServicesErrorStatus.NOERROR
            }
        }
    }

    override fun onCleared() {
        WebServices.postImageResponse.removeObserver(postImageResponseObserver)
        super.onCleared()
    }
}
