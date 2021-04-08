package com.example.chessboard_importer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.chessboard_importer.databinding.ActivityPhotoPreviewBinding
import org.json.JSONObject


private lateinit var binding: ActivityPhotoPreviewBinding
class PhotoPreviewActivity : AppCompatActivity() {
    private val urlLogin = "http://10.0.2.2:8000/login"
    private val urlRefresh = "http://10.0.2.2:8000/refresh"
    private val urlPhoto = "http://10.0.2.2:8000/photo"
    private val urlHello = "http://10.0.2.2:8000/"
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getAccessToken()

        binding.ivPhoto.setImageBitmap(photoBitmap)

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        binding.btnAccept.setOnClickListener {
            uploadImage()
        }
    }

    private fun getAccessToken(){

        val jsonData = HashMap<String,String>()
        jsonData["username"] = "chessuser"
        jsonData["password"] = "magnusgruchala"
        val jsonRequest = JSONObject(jsonData as Map<String,String>)
        val accessTokenRequest = object : JsonObjectRequest(Request.Method.POST,urlLogin,jsonRequest,
                Response.Listener {
                    accessToken = it.getString("access_token")
                    refreshToken = it.getString("refresh_token")
                    Log.d("Response","Access token: ${accessToken}, " +
                            "Refresh token: ${refreshToken}")
                                  },
                Response.ErrorListener { Log.d("Response","$it") }
        ){}
        ServerConnector.getInstance(this).addToRequestQueue(accessTokenRequest)
    }



    private fun uploadImage(){
        val imagePostRequest = object : ImagePostRequest(Request.Method.POST, urlPhoto,
                Response.Listener {
                    Log.d("Response", "response is: ${String(it.data)}")
                    val width = JSONObject(String(it.data)).getString("width").toInt()
                    val height = JSONObject(String(it.data)).getString("height").toInt()
                    binding.tvResponse.text = "Width: $width Height: $height"
                },
                Response.ErrorListener { Log.d("ErrorResponse", "ERROR") }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String,String>()
                header["Authorization"] = "Bearer $accessToken"
                return header.toMutableMap()
            }

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

    private fun getHello(){
        val getHelloRequest = JsonObjectRequest(Request.Method.GET,urlHello,null,
        Response.Listener<JSONObject> {
            binding.tvResponse.text = "$it"
        },
        Response.ErrorListener {
            binding.tvResponse.text = "DUPA"
            Log.d("Response","$it")
        })

        ServerConnector.getInstance(this).addToRequestQueue(getHelloRequest)

    }
}
