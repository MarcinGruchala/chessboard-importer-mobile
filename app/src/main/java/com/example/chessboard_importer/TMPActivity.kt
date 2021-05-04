package com.example.chessboard_importer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.chessboard_importer.databinding.ActivityTmpBinding
import org.json.JSONObject


private lateinit var binding: ActivityTmpBinding
class TMPActivity : AppCompatActivity() {
    private val urlLogin = "http://10.0.2.2:8000/login"
    private val urlRefresh = "http://10.0.2.2:8000/refresh"
    private val urlPhoto = "http://10.0.2.2:8000/photo"
    private val urlHello = "http://10.0.2.2:8000/"
    private val localUrlRefresh = "http://ip/refresh"
    private val localUrlPhoto = "http://ip/photo"
    private val localUrlLogin = "http://ip/login"
    private val localUrlHello = "http://ip/"
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTmpBinding.inflate(layoutInflater)
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

        val jsonData = HashMap<String, String>()
        jsonData["username"] = com.example.chessboard_importer.BuildConfig.APIUSERNAME
        jsonData["password"] =  com.example.chessboard_importer.BuildConfig.APIPASSWORD
        val jsonRequest = JSONObject(jsonData as Map<String, String>)
        val accessTokenRequest = object : JsonObjectRequest(Request.Method.POST,
            localUrlLogin,
            jsonRequest,
            Response.Listener {
                accessToken = it.getString("access_token")
                refreshToken = it.getString("refresh_token")
                Log.d(
                    "Response", "Access token: ${accessToken}, " +
                            "Refresh token: ${refreshToken}"
                )
            },
            Response.ErrorListener { Log.d("Response", "$it") }
        ){}
        ServerConnector.getInstance(this).addToRequestQueue(accessTokenRequest)
    }



    private fun uploadImage(){
        val imagePostRequest = object : ImagePostRequest(Request.Method.POST, localUrlPhoto,
            Response.Listener {
                Log.d("Response", "response is: ${String(it.data)}")
                val fen = JSONObject(String(it.data)).getString("fen")
                binding.tvResponse.text = "FEN: $fen"
            },
            Response.ErrorListener { Log.d("UploadImageResponse", "$it") }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
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

        Log.d("TMPActivity", "upload timeout: ${imagePostRequest.timeoutMs}")
        ServerConnector.getInstance(this).addToRequestQueue(imagePostRequest)
    }

    private fun getHello(){
        val getHelloRequest = JsonObjectRequest(Request.Method.GET, localUrlHello, null,
            Response.Listener<JSONObject> {
                binding.tvResponse.text = "$it"
            },
            Response.ErrorListener {
                binding.tvResponse.text = "DUPA"
                Log.d("Response", "$it")
            })

        ServerConnector.getInstance(this).addToRequestQueue(getHelloRequest)

    }
}
