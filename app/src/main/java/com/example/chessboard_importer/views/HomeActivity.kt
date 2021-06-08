package com.example.chessboard_importer.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.chessboard_importer.R
import com.example.chessboard_importer.databinding.ActivityHomeBinding
import com.example.chessboard_importer.viewmodels.HomeActivityViewModel
import com.example.chessboard_importer.views.camera_module.CameraActivity

import com.example.chessboard_importer.webservices.WebServicesErrorStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getAccessToken()

        val imageUriObserver = Observer<Uri?> { newUri -> viewModel.createPhotoData(newUri!!) }
        viewModel.importedImageUri.observe(this,imageUriObserver)

        val apiConnectionObserver = Observer<WebServicesErrorStatus> { errorStatus ->
            if (errorStatus ==  WebServicesErrorStatus.CONNECTIONERROR){
                showApiConnectionErrorDialogWindow()
            }
        }
        viewModel.apiConnectionErrorStatus.observe(this,apiConnectionObserver)

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

    private fun showApiConnectionErrorDialogWindow(){
        val errorMessageDialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
            .setTitle(R.string.ErrorMessageTitle)
            .setMessage(R.string.ErrorMessageApiConnection)
        errorMessageDialog.show()
    }

    companion object {
        private const val IMPORT_PICTURE_REQUEST_CODE = 20
    }
}
