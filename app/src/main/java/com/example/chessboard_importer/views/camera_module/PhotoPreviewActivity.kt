package com.example.chessboard_importer.views.camera_module

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chessboard_importer.databinding.ActivityPhotoPreviewBinding
import com.example.chessboard_importer.views.ChessboardActivity
import com.example.chessboard_importer.views.photoBitmap

class PhotoPreviewActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityPhotoPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivPhotoPreview.setImageBitmap(photoBitmap)

        binding.btnAcceptPhoto.setOnClickListener {
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
}
