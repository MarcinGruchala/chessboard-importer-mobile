package com.example.chessboard_importer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chessboard_importer.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnTakePhoto.setOnClickListener {
            Intent(this, PhotoPreviewActivity::class.java ).also {
                startActivity(it)
            }
        }

        binding.btnImportPhoto.setOnClickListener {
            Intent(this, PhotoPreviewActivity::class.java ).also {
                startActivity(it)
            }
        }
    }
}