package com.example.chessboard_importer.views

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.chessboard_importer.databinding.ActivityHomeBinding
import com.example.chessboard_importer.viewmodels.HomeActivityViewModel
import com.example.chessboard_importer.views.camera_module.CameraActivity


lateinit var photoBitmap: Bitmap

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel.getAccessToken()
        val imageUriObserver = Observer<Uri?> { newUri -> viewModel.createPhotoData(newUri!!) }
        viewModel.importedImageUri.observe(this,imageUriObserver)

        setUpClickListeners()
    }

    private fun setUpClickListeners(){
        binding.btnTakePhoto.setOnClickListener {
            Intent(this, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnImportPhoto.setOnClickListener {
            val importPictureIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(importPictureIntent, IMPORT_PICTURE_REQUEST_CODE)
        }

        binding.btnChessboard.setOnClickListener {
            Intent(this, ChessboardActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMPORT_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val photoUri: Uri? = data?.data
            if (photoUri != null){
                viewModel.importedImageUri.value = photoUri
            }
            Intent(this, ChessboardActivity::class.java).also { startActivity(it) }
        }
    }

    companion object {
        private const val IMPORT_PICTURE_REQUEST_CODE = 20
    }
}
