package com.example.chessboard_importer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chessboard_importer.databinding.ActivityPhotoPreviewBinding
import kotlinx.android.synthetic.main.activity_photo_preview.*

class PhotoPreviewActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityPhotoPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivPhotoPreview.setImageBitmap(photoBitmap)

        binding.btnAcceptPhoto.setOnClickListener { Intent(this,TMPActivity::class.java).also { startActivity(it) } }

        binding.btnRetakePhoto.setOnClickListener { finish() }
    }
}
