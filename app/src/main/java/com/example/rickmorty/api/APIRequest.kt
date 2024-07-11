package com.example.rickmorty.api

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.rickmorty.utils.Constants
import com.example.rickmorty.utils.Utils
import org.json.JSONException
import org.json.JSONObject

class APIRequest(private val context: Context) {
    private var jsonURL: String? = null
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val header: MutableMap<String, String> = HashMap()
    private val parameters = JSONObject()
    var method: Int = Request.Method.GET
    var path: String = ""

    fun setMethod(method: Int?) {
        if (method != null) {
            this.method = method
        }
    }

    fun updatePath(path: String?) {
        this.path = path ?: ""
    }

    fun setCustomUrl(url: String?) {
        this.jsonURL = url
    }

    fun addHeader(key: String, value: String) {
        header[key] = value
    }

    fun addParam(key: String, value: String) {
        try {
            parameters.put(key, value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun executeRequest(callback: VolleyCallback) {
        if (Utils.isNetworkAvailable(context)) {

            val url: String = "${Constants.BASE_URL}$path"
            Utils.log("APIRequest", "--------------------------URL----------------------------")
            Utils.log("APIRequest", url)

            try {
                Utils.log(
                    "APIRequest",
                    "--------------------------Params----------------------------"
                )
                Utils.log("APIRequest", parameters.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }


            header["Content-Type"] = "application/json"
            Utils.getSharedPreference().getString("token", null)?.let {
                header["Authorization"] = "Bearer $it"
            }


            requestQueue.cache.clear()


            val jsonRequest = object : JsonObjectRequest(method, url, parameters,
                { response ->
                    try {
                        Utils.log(
                            "APIRequest",
                            "--------------------------Response----------------------------"
                        )
                        Utils.log("APIRequest", response.toString())
                        callback.getSuccessResponse(response, "Success")
                    } catch (e: Exception) {
                        callback.getErrorResponse(e.message ?: "Unknown error")
                    }
                },
                { error ->
                    try {
                        Utils.log(
                            "APIRequest",
                            "--------------------------Response Error----------------------------"
                        )
                        Utils.log("APIRequest", error.message ?: "Unknown error")
                        callback.getErrorResponse(error.message ?: "Something Went Wrong!")
                    } catch (e: Exception) {
                        Utils.log("APIRequest", "Error " + e.message)
                        callback.getErrorResponse("Something Went Wrong!")
                    }
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    Utils.log(
                        "APIRequest",
                        "--------------------------Header----------------------------"
                    )
                    Utils.log("APIRequest", header.toString())
                    return header
                }
            }

            jsonRequest.retryPolicy = DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(jsonRequest)
        } else {
            callback.getErrorResponse("Network connectivity issue.")
        }
    }

    interface VolleyCallback {
        fun getSuccessResponse(response: JSONObject, message: String)
        fun getErrorResponse(error: String)
    }
}
