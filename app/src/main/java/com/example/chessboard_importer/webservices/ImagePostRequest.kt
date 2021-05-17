package com.example.chessboard_importer

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.*
import java.lang.Exception

open class ImagePostRequest(
         method: Int,
         url: String,
         listener: Response.Listener<NetworkResponse>,
         errorListener: Response.ErrorListener
 ) : Request<NetworkResponse>(method,url,errorListener) {
    private var responseListener: Response.Listener<NetworkResponse>? = null

    init {
        this.responseListener = listener
    }

    private var headers: Map<String,String>? = null
    private val divider = "--"
    private val ending = "\r\n"
    private val boundary = "imagePostRequest${System.currentTimeMillis()}"

    override fun getHeaders(): MutableMap<String, String> {
        return when(headers){
            null -> super.getHeaders()
            else -> headers!!.toMutableMap()
        }
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream = DataOutputStream(byteArrayOutputStream)
        try {
            if (params != null && params!!.isNotEmpty()) {
                processParams(dataOutputStream, params!!,paramsEncoding)
            }
            val data = getByteData() as? Map<String,FileData>?
            if (data != null && data.isNotEmpty()){
                processData(dataOutputStream,data)
            }
            dataOutputStream.writeBytes(divider+boundary+divider+ending)
            return  byteArrayOutputStream.toByteArray()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return  super.getBody()
    }

    open fun getByteData(): Map<String, Any>? = null

    override fun parseNetworkResponse(response: NetworkResponse?): Response<NetworkResponse> {
        return  try {
            Response.success(response,HttpHeaderParser.parseCacheHeaders(response))
        }catch (e: Exception){
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse?) {
        responseListener?.onResponse(response)
    }

    override  fun deliverError(error: VolleyError){
        errorListener?.onErrorResponse(error)
    }

    private fun processParams(dataOutputStream: DataOutputStream, params: Map<String,String>, encoding: String){
        try {
            params.forEach{
                dataOutputStream.writeBytes(divider+boundary+ending)
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"${it.key}\"$ending")
                dataOutputStream.writeBytes(ending)
                dataOutputStream.writeBytes(it.value + ending)
            }
        }catch (e: UnsupportedEncodingException){
            throw RuntimeException("Unsupported encoding not supported: $encoding with error: ${e.message}", e)
        }
    }

    private fun processData(dataOutputStream: DataOutputStream, data: Map<String,FileData>){
        data.forEach {
            val dataFile = it.value
            dataOutputStream.writeBytes("$divider$boundary$ending")
            dataOutputStream.writeBytes("Content-Disposition: " +
                    "form-data; " +
                    "name=\"${it.key}\"; " +
                    "filename=\"${dataFile.fileName}\"$ending")
            if (dataFile.type.trim().isNotEmpty()){
                dataOutputStream.writeBytes("Content-Type: ${dataFile.type}$ending")
            }
            dataOutputStream.writeBytes(ending)
            val fileInputStream = ByteArrayInputStream(dataFile.data)
            var bytesAvailable = fileInputStream.available()
            val maxBufferSize = 1024 * 1024
            var bufferSize = kotlin.math.min(bytesAvailable, maxBufferSize)
            val buffer = ByteArray(bufferSize)
            var bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0){
                dataOutputStream.write(buffer,0,bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = kotlin.math.min(bytesAvailable,maxBufferSize)
                bytesRead = fileInputStream.read(buffer,0, bufferSize)
            }
            dataOutputStream.writeBytes(ending)
        }
    }
}

class FileData(val fileName: String, val data: ByteArray, val type: String)
