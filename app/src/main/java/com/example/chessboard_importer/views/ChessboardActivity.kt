package com.example.chessboard_importer.views


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.chessboard_importer.R
import com.example.chessboard_importer.databinding.ActivityChessboardBinding
import com.example.chessboard_importer.models.ChessPlayer
import com.example.chessboard_importer.viewmodels.ChessboardActivityViewModel
import com.example.chessboard_importer.webservices.WebServicesErrorStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChessboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChessboardBinding
    private val viewModel: ChessboardActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChessboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val progressBar = Dialog(this)
        progressBar.setContentView(R.layout.dialog_progress)

        setUpClickListeners()
        binding.chessBoard.setFen(viewModel.getBoardFen())


        val isImageUploadingObserver = Observer<Boolean> { uploadState ->
            if (uploadState){
                progressBar.show()
            }
        }
        viewModel.isImageUploading.observe(this,isImageUploadingObserver)


        val newFenObserver = Observer<String> { newFen ->
            binding.chessBoard.setFen(newFen)
            viewModel.updateBoardFen(newFen)
            progressBar.dismiss()
            showPlayerChoiceDialogWindow()
        }
        viewModel.newFen.observe(this, newFenObserver)

        val imageUploadErrorObserver = Observer<WebServicesErrorStatus> { errorState ->
            when (errorState) {
                WebServicesErrorStatus.PHOTOERROR -> {
                    progressBar.dismiss()
                    showWebServicePhotoErrorDialogWindow()
                }
                WebServicesErrorStatus.TIMEOUTERROR -> {
                    progressBar.dismiss()
                    showWebServiceTimeoutErrorDialogWindow()
                }
                WebServicesErrorStatus.CONNECTIONERROR -> {
                    showWebServicesConnectionErrorDialogWindow()
                }
                else -> {}
            }
        }
        viewModel.imageUploadErrorStatus.observe(this, imageUploadErrorObserver)

        viewModel.checkIfNewPhotoData()
    }


    private fun setUpClickListeners() {

        binding.btnGoHome.setOnClickListener {
            finish()
        }
        binding.btnPlayerChoice.setOnClickListener{
            showPlayerChoiceDialogWindow()
        }
        binding.btnCopyFEN.setOnClickListener {
            viewModel.updateBoardFen(binding.chessBoard.getFen())
            viewModel.setPlayerTurn()
            viewModel.copyFen()
            Toast.makeText(this, "Copied Fen", Toast.LENGTH_LONG).show()
        }
        binding.btnConnect.setOnClickListener {
            viewModel.updateBoardFen(binding.chessBoard.getFen())

            viewModel.setPlayerTurn()
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://lichess.org/analysis/master?fen=${viewModel.getBoardFen()}"
                )
            ).also { startActivity(it) }
        }
    }

    private fun showPlayerChoiceDialogWindow() {
        val options = arrayOf(ChessPlayer.BLACK.pieceColor, ChessPlayer.WHITE.pieceColor)
        var playerChoice = ChessPlayer.WHITE
        val playerChoiceDialog =
            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Next move is")
                .setSingleChoiceItems(options, -1) { _, which: Int ->
                    when (which) {

                        0 -> { playerChoice = ChessPlayer.BLACK }
                        1 -> { playerChoice = ChessPlayer.WHITE }
                    }
                    Toast.makeText(this, "Next move is ${options[which]}", Toast.LENGTH_SHORT)
                        .show()
                }
                .setPositiveButton("Accept") { _, _ ->

                    viewModel.playerChoice = playerChoice
                }
                .setNegativeButton("Skip") { _, _ -> }
        playerChoiceDialog.show()
    }

    private fun showWebServicePhotoErrorDialogWindow(){
        val errorMessageDialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
            .setTitle(R.string.ErrorMessageTitle)
            .setMessage(R.string.ErrorMessagePhotoError)
        errorMessageDialog.show()
    }

    private fun showWebServiceTimeoutErrorDialogWindow(){
        val errorMessageDialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
            .setTitle(R.string.ErrorMessageTitle)
            .setMessage(R.string.ErrorMessageTimeoutError)
        errorMessageDialog.show()

    }

    private fun showWebServicesConnectionErrorDialogWindow(){
        val errorMessageDialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
            .setTitle(R.string.ErrorMessageTitle)
            .setMessage(R.string.ErrorMessageApiConnection)
        errorMessageDialog.show()
    }
}
