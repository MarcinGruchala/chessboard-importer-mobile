package com.example.chessboard_importer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.example.chessboard_importer.databinding.ActivityPhotoPreviewBinding
import org.json.JSONObject


private lateinit var binding: ActivityPhotoPreviewBinding
class PhotoPreviewActivity : AppCompatActivity() {
    private val url = "https://chessboard-importer.herokuapp.com/photo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.ivPhoto.setImageBitmap(photoBitmap)

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        binding.btnAccept.setOnClickListener {

            val imagePostRequest = object : ImagePostRequest(Request.Method.POST, url,
                    Response.Listener {
                        Log.d("Response", "response is: ${String(it.data)}")
                        val width = JSONObject(String(it.data)).getString("width").toInt()
                        val height = JSONObject(String(it.data)).getString("height").toInt()
                        binding.tvResponse.text = "Width: $width Height: $height"
                    },
                    Response.ErrorListener { Log.d("ErrorResponse", "ERROR") }
            ){
                override fun getByteData(): MutableMap<String, FileData>? {
                    var params = HashMap<String, FileData>()
                    if (photoData==null)
                        Log.d("Error", "photoData = null")
                    params["file"] = FileData("image", photoData!!, "jpeg")
                    return params
                }
            }
            ServerConnector.getInstance(this).addToRequestQueue(imagePostRequest)
        }
    }
}
