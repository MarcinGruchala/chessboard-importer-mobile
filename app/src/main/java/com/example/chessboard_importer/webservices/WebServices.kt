package com.example.chessboard_importer.webservices

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.chessboard_importer.BuildConfig
import com.example.chessboard_importer.FileData
import com.example.chessboard_importer.ImagePostRequest
import com.example.chessboard_importer.repository.ApplicationRepository
import org.json.JSONObject

object WebServices {

    var tokenResponse: TokenResponse = TokenResponse("","")
    val postImageResponse: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getAccessToken(applicationContext: Context) {
        val jsonData = HashMap<String, String>()
        jsonData["username"] = BuildConfig.APIUSERNAME
        jsonData["password"] = BuildConfig.APIPASSWORD
        val jsonRequest = JSONObject(jsonData as Map<String, String>)
        val accessTokenRequest = object : JsonObjectRequest(
            Method.POST,
            UrlAddresses.LOGIN,
            jsonRequest,
            Response.Listener {
                tokenResponse.accessToken = it.getString("access_token")
                tokenResponse.refreshToken = it.getString("refresh_token")
                Log.d(
                    "Access Token Response", "AccessToken Response: ${tokenResponse.accessToken}, " +
                            "Refresh token: ${tokenResponse.refreshToken}"
                )
            },
            Response.ErrorListener { Log.d("Access Token Response: ", "$it") }
        ) {}
        ServerConnector.getInstance(applicationContext).addToRequestQueue(accessTokenRequest)
    }

    fun postImage(applicationContext: Context){
        val imagePostRequest = object : ImagePostRequest(
            Method.POST, UrlAddresses.PHOTO,
            Response.Listener {
                Log.d("Response", "response is: ${String(it.data)}")
                postImageResponse.value = JSONObject(String(it.data)).getString("fen")
            },
            Response.ErrorListener {
                Log.d("UploadImageResponse", "$it")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["Authorization"] = "Bearer ${tokenResponse.accessToken}"
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
