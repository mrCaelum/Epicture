package eu.kleiver.epicture

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject
import okhttp3.*
import java.io.IOException

object ImgurAPI {
    private const val clientId: String = "8a2609811db5811"
    private const val clientSecret: String = "1c7a3990d3bbcf7ecc00c1c5e7b9409e35a9b7a0"
    private const val host: String = "api.imgur.com"
    private const val apiVersion: String = "3"
    private val httpClient: OkHttpClient = OkHttpClient.Builder().build()
    var data: HashMap<String, String> = HashMap()
    var receivedData: JSONObject? = null

    /*
     * Ask Imgur credentials by calling the web browser
     * @param baseContext: Login intent base context
     */
    fun askCredentials(baseContext: Context)
    {
        startActivity(baseContext, Intent(Intent.ACTION_VIEW, Uri.parse("https://api.imgur.com/oauth2/authorize?response_type=token&client_id=" + this.clientId)), null)
    }

    fun parseData(data: String)
    {
        val query = data.split('#')[1]
        for (value in query.split("&")) {
            val pair = value.split("=")
            this.data[pair[0]] = pair[1]
        }
    }

    private fun buildGetRequest(url: HttpUrl): Request?
    {
        return Request.Builder()
            .url(url)
            .header("Authorization", "Client-ID " + this.clientId)
            .header("Authorization", "Bearer " + this.data["access_token"])
            .header("User-Agent", "Epicture")
            .get()
            .build()
    }

    private fun asyncRequest(req: Request)
    {
        httpClient.newCall(req).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                ImgurAPI.receivedData = JSONObject(response.body!!.string())
            }

            override fun onFailure(call: Call, e: IOException) {
                ImgurAPI.receivedData = null
            }
        })
    }

    fun getUserImages(page: Int = 0, resolve: () -> Unit, reject: () -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("account")
            .addPathSegment(data["account_username"]!!)
            .addPathSegment("images")
            .addPathSegment(page.toString())
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            asyncRequest(it)
            if (this.receivedData?.getBoolean("success") == true)
                return resolve()
        }
        return reject()
    }
}