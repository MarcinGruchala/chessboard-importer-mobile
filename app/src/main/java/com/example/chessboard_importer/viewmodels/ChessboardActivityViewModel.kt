package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.chessboard_importer.models.ChessPlayer
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.webservices.WebServices

class ChessboardActivityViewModel(application: Application) : AndroidViewModel(application){
    private val applicationContext by lazy { application.applicationContext }
    val newFen: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val postImageResponseObserver = Observer<String> { response ->
        Log.d("ChessboardActivity", "new fen=  $response")
        newFen.value = response
    }

    init {
        WebServices.postImageResponse.observeForever(postImageResponseObserver)
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

    fun setPlayerTurn(player: ChessPlayer){
        ApplicationRepository.board.setPlayerTurn(player)
    }

    fun checkIfNewPhotoData(){
        if (ApplicationRepository.newPhotoData){
            WebServices.postImage(applicationContext)
            ApplicationRepository.newPhotoData = false
        }
    }

    override fun onCleared() {
        WebServices.postImageResponse.removeObserver(postImageResponseObserver)
        super.onCleared()
    }
}
