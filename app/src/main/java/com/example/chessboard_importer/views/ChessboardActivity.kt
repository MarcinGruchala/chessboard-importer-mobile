package com.example.chessboard_importer.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.chessboard_importer.databinding.ActivityChessboardBinding
import com.example.chessboard_importer.models.Board
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.viewmodels.ChessboardActivityViewModel

class ChessboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChessboardBinding
    private val viewModel: ChessboardActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChessboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpClickListeners()
        binding.chessBoard.setFen(Board.STARTING_POSITION)

        val newFenObserver = Observer<String> { newFen -> binding.chessBoard.setFen(newFen) }
        viewModel.newFen.observe(this, newFenObserver)
    }

    private fun setUpClickListeners() {
        binding.btnGoHome.setOnClickListener { finish() }
        binding.btnConnect.setOnClickListener {
            viewModel.uploadImage()
        }
    }
}
