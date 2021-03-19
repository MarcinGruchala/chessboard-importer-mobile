package com.example.chessboard_importer

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.chessboard_importer.databinding.ActivityMainBinding
import java.io.File

private const val TAKE_PHOTO_REQUEST_CODE=42
private const val FILE_NAME = "photo.jpg"
lateinit var photoFile: File

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnTakePhoto.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            val fileProvider  = FileProvider.getUriForFile(this,"com.example.provider.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE)
        }

        binding.btnImportPhoto.setOnClickListener {
            Intent(this, PhotoPreviewActivity::class.java ).also {
                startActivity(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Intent(this, PhotoPreviewActivity::class.java ).also {
                startActivity(it)
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storageDirectory)
    }
}