package eu.kleiver.epicture

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

object ImgurAPI {
    private const val clientId: String = "8a2609811db5811"
    private const val clientSecret: String = "1c7a3990d3bbcf7ecc00c1c5e7b9409e35a9b7a0"
    private const val host: String = "api.imgur.com"
    private const val apiVersion: String = "3"
    private val httpClient: OkHttpClient = OkHttpClient.Builder().build()
    var data: HashMap<String, String> = HashMap()

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

    private fun getJsonLink(jsonObject: JSONObject): String
    {
        return if (jsonObject.has("cover")) {
            ("https://i.imgur.com/" + jsonObject.getString("cover") + "." + jsonObject.getString("type").toString().split('/')[1])
        } else {
            jsonObject.getString("link")
        }
    }

    fun jsonToImageList(jsonArray: JSONArray, useFastLink: Boolean = false): ArrayList<Image>
    {
        val imageList: ArrayList<Image> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val newImage = Image(
                id = jsonObject.getString("id"),
                is_album = false,
                link = if (useFastLink) { getJsonLink(jsonObject) } else { jsonObject.getString("link") },
                images = null
            )
            if (jsonObject.has("is_album") && (!useFastLink || (useFastLink && !jsonObject.has("cover")))) {
                newImage.is_album = jsonObject.getBoolean("is_album")
            }
            if (newImage.is_album) {
                newImage.images = jsonToImageList(jsonObject.getJSONArray("images"))
            }
            imageList.add(newImage)
        }
        return imageList
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

    private fun asyncRequest(req: Request, resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        httpClient.newCall(req).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                return try {
                    val res = JSONObject(response.body!!.string())
                    if (res.getBoolean("success")) {
                        resolve(res)
                    } else {
                        reject(Exception())
                    }
                } catch (e: Exception) {
                    reject(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                return reject(e)
            }
        })
    }

    fun favorite(id: String, resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("image")
            .addPathSegment(id)
            .addPathSegment("favorite")
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }

    fun getUserAvatar(resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("account")
            .addPathSegment(data["account_username"]!!)
            .addPathSegment("avatar")
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }

    fun getUserImages(page: Int = 0, resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
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
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }

    fun getUserFavorites(page: Int = 0, resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("account")
            .addPathSegment(data["account_username"]!!)
            .addPathSegment("favorites")
            .addPathSegment(page.toString())
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }

    fun searchImages(value: String, resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("gallery")
            .addPathSegment("search")
            .addEncodedQueryParameter("q", value)
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }

    fun getUserBio(resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("account")
            .addPathSegment(data["account_username"]!!)
            .addPathSegment("bio")
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }
}