package com.example.chessboard_importer.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.chessboard_importer.BuildConfig
import com.example.chessboard_importer.models.Board
import com.example.chessboard_importer.webservices.ServerConnector
import com.example.chessboard_importer.webservices.UrlAddresses
import org.json.JSONObject

object ApplicationRepository {

    init {
        Log.d("ApplicationRepository", "Repository init")
    }

    val photoData: MutableLiveData<ByteArray?> by lazy { MutableLiveData<ByteArray?>() }
    var accessToken: String? = null
    var refreshToken: String? = null

    fun getAccessToken(context: Context) {
        val jsonData = HashMap<String, String>()
        jsonData["username"] = BuildConfig.APIUSERNAME
        jsonData["password"] = BuildConfig.APIPASSWORD
        val jsonRequest = JSONObject(jsonData as Map<String, String>)
        val accessTokenRequest = object : JsonObjectRequest(
            Request.Method.POST,
            UrlAddresses.LOGIN,
            jsonRequest,
            Response.Listener {
                accessToken = it.getString("access_token")
                refreshToken = it.getString("refresh_token")
                Log.d(
                    "Response", "AccessToken Response: ${accessToken}, " +
                            "Refresh token: ${refreshToken}"
                )
            },
            Response.ErrorListener { Log.d("AccessToken Response: ", "$it") }
        ) {}
        ServerConnector.getInstance(context).addToRequestQueue(accessTokenRequest)
    }
}
