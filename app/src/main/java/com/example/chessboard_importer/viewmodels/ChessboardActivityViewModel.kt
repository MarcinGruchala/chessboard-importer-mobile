package com.example.chessboard_importer.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.example.chessboard_importer.FileData
import com.example.chessboard_importer.ImagePostRequest
import com.example.chessboard_importer.repository.ApplicationRepository
import com.example.chessboard_importer.webservices.ServerConnector
import com.example.chessboard_importer.webservices.UrlAddresses
import org.json.JSONObject

class ChessboardActivityViewModel(application: Application) : AndroidViewModel(application){
    private val applicationContext by lazy { application.applicationContext }
    val newFen: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun uploadImage() {
        Log.d("ChessboardActivity", "uploadImage function")
        Toast.makeText(
            applicationContext, "Image is uploading and processing, it may take a moment",
            Toast.LENGTH_LONG
        ).show()
        val imagePostRequest = object : ImagePostRequest(
            Method.POST, UrlAddresses.PHOTO,
            Response.Listener {
                Log.d("Response", "response is: ${String(it.data)}")
                newFen.value = JSONObject(String(it.data)).getString("fen")

                Toast.makeText(applicationContext, "FEN: ${newFen.value} ", Toast.LENGTH_LONG)
                    .show()
            },
            Response.ErrorListener {
                Log.d("UploadImageResponse", "$it")
                Toast.makeText(
                    applicationContext,
                    "Error while uploading a picture: $it",
                    Toast.LENGTH_LONG
                ).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["Authorization"] = "Bearer ${ApplicationRepository.accessToken}"
                return header.toMutableMap()
            }

            override fun getByteData(): MutableMap<String, FileData> {
                val params = HashMap<String, FileData>()
                if (ApplicationRepository.photoData.value == null)
                    Log.d("Error", "photoData = null")
                params["file"] = FileData("image", ApplicationRepository.photoData.value!!, "jpeg")
                return params
            }

        }

        imagePostRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 300000
            }

            override fun getCurrentRetryCount(): Int {
                return 300000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        }

        ServerConnector.getInstance(applicationContext).addToRequestQueue(imagePostRequest)
    }
}
