package com.example.minimoneybox.utilities

import com.example.minimoneybox.transferInfo.NetworkInfo
import com.example.minimoneybox.transferInfo.RequestMethod
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit


class NetworkTask {

    //this method was created to avoid duplicated code
    //there are a number of default headers that can be used as headers in every request
    //the specific data to specialise requests can be passed in as networkInfo
    fun runNetworkTask(networkInfo: NetworkInfo) : String {

        //default headers used for all requests
        var request = Request.Builder()
            .url("https://api-test01.moneyboxapp.com/" + networkInfo.urlPath)
            .addHeader("AppId", "3a97b932a9d449c981b595")
            .addHeader("appVersion", "5.10.0")
            .addHeader("apiVersion", "3.0.0")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")

        if (networkInfo.bearer != null) {
            request.addHeader("Authorization", "Bearer " + networkInfo.bearer)
        }

        //extension of functionality through extra requests can be processed here as long as the enum RequestMethod is also updated
        when (networkInfo.requestMethod) {
            RequestMethod.GET -> request.get()
            RequestMethod.POST -> {
                val mediaType = "application/x-www-form-urlencoded".toMediaType()
                val body = networkInfo.bodyInfo!!.toRequestBody(mediaType)
                request.post(body)
            }
        }

        //ensures that the client does not time out before request is completed
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val body = client.newCall(request.build()).execute().body
        when (body != null) {
            true -> return body.string()
            false -> return ""
        }
    }
}