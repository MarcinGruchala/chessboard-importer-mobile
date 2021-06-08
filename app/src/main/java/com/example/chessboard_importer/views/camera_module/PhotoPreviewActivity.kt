package com.example.chessboard_importer.views.camera_module

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.chessboard_importer.databinding.ActivityPhotoPreviewBinding
import com.example.chessboard_importer.viewmodels.PhotoPreviewViewModel
import com.example.chessboard_importer.views.ChessboardActivity

class PhotoPreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoPreviewBinding
    private val viewModel: PhotoPreviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpImageView()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnAcceptPhoto.setOnClickListener {
            viewModel.notifyNewPhoto()
            Intent(this, ChessboardActivity::class.java).also {
                startActivity(it).also {
                    finish()
                }
            }
        }

        binding.btnRetakePhoto.setOnClickListener {
            Intent(this, CameraActivity::class.java).also {
                startActivity(it).also {
                    finish()
                }
            }
        }
    }

    private fun setUpImageView() {
        binding.ivPhotoPreview.setImageBitmap(viewModel.getPhotoBitmap())
    }
}
