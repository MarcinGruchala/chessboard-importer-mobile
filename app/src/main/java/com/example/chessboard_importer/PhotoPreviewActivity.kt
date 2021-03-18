package com.example.chessboard_importer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chessboard_importer.databinding.ActivityPhotoPreviewBinding

private lateinit var binding: ActivityPhotoPreviewBinding
class PhotoPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnGoBack.setOnClickListener {
            finish()
        }
    }
}