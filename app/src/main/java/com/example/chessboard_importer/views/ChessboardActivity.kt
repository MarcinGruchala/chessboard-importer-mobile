package com.example.chessboard_importer.views

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChessboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChessboardBinding
    private val viewModel: ChessboardActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChessboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpClickListeners()
        binding.chessBoard.setFen(viewModel.getBoardFen())

        val newFenObserver = Observer<String> { newFen ->
            binding.chessBoard.setFen(newFen)
            viewModel.updateBoardFen(newFen)
        }
        viewModel.newFen.observe(this, newFenObserver)

        viewModel.checkIfNewPhotoData()
    }

    private fun setUpClickListeners() {
        binding.btnGoHome.setOnClickListener { finish() }
        binding.btnCopyFEN.setOnClickListener {
            viewModel.updateBoardFen(binding.chessBoard.getFen())
            viewModel.copyFen()
            Toast.makeText(this, "Copied Fen", Toast.LENGTH_LONG).show()
        }
        binding.btnConnect.setOnClickListener {
            viewModel.updateBoardFen(binding.chessBoard.getFen())
            showDialogWindow()
        }
    }

    private fun showDialogWindow() {
        val options = arrayOf(ChessPlayer.BLACK.pieceColor, ChessPlayer.WHITE.pieceColor)
        val playerChoiceDialog =
            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Next move is")
                .setSingleChoiceItems(options, -1) { _, which: Int ->
                    when (which) {
                        0 -> { viewModel.setPlayerTurn(ChessPlayer.BLACK) }
                        1 -> { viewModel.setPlayerTurn(ChessPlayer.WHITE) }
                    }
                    Toast.makeText(this, "Next move is ${options[which]}", Toast.LENGTH_SHORT)
                        .show()
                }
                .setPositiveButton("Accept") { _, _ ->
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://lichess.org/analysis/master?fen=${viewModel.getBoardFen()}"
                        )
                    ).also { startActivity(it) }
                }
                .setNegativeButton("Cancel") { _, _ -> }
        playerChoiceDialog.show()
    }
}
