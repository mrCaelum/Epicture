package eu.kleiver.epicture

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

/**
 * An object that implement the Imgur API
 */
object ImgurAPI {
    private const val clientId: String = "8a2609811db5811"
    private const val clientSecret: String = "1c7a3990d3bbcf7ecc00c1c5e7b9409e35a9b7a0"
    private const val host: String = "api.imgur.com"
    private const val apiVersion: String = "3"
    private val httpClient: OkHttpClient = OkHttpClient.Builder().build()
    var data: HashMap<String, String> = HashMap()

    /**
     * Call the Imgur login web intent
     *
     * @param baseContext The base context to call the intent
     */
    fun askCredentials(baseContext: Context)
    {
        startActivity(baseContext, Intent(Intent.ACTION_VIEW, Uri.parse("https://api.imgur.com/oauth2/authorize?response_type=token&client_id=" + this.clientId)), null)
    }

    /**
     * Parse received data from login
     * The resulting data is stored in the object data value
     *
     * @param data the data string to parse
     */
    fun parseData(data: String)
    {
        val query = data.split('#')[1]
        for (value in query.split("&")) {
            val pair = value.split("=")
            this.data[pair[0]] = pair[1]
        }
    }

    /**
     * Get the string from a JSONObject
     *
     * @param jsonObject The JSONObject
     * @return the link string
     */
    private fun getJsonLink(jsonObject: JSONObject): String
    {
        return if (jsonObject.has("cover")) {
            ("https://i.imgur.com/" + jsonObject.getString("cover") + "." + jsonObject.getString("type").toString().split('/')[1])
        } else {
            jsonObject.getString("link")
        }
    }

    /**
     * Converts a JSONArray to a list of Images
     *
     * @param jsonArray the JSONArray object
     * @param useFastLink if true, get the cover image as link image
     * @return The list of Images
     */
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

    /**
     * Build a get request from an url
     *
     * @param url the HttpUrl object
     * @return Builded request
     */
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

    /**
     * Launch an asynchronous request from a builded request.
     *
     * @param req The request to launch
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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

    /**
     * Retrieve an Imgur image from its ID
     *
     * @param id The ID of the image as string
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
    fun getImage(id: String, resolve: (JSONObject) -> Unit, reject: (Exception) -> Unit)
    {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegment(apiVersion)
            .addPathSegment("image")
            .addPathSegment(id)
            .build()
        val req: Request? = buildGetRequest(url)
        req?.let {
            return asyncRequest(it, resolve, reject)
        }
        return reject(Exception())
    }

    /**
     * Favorite an Imgur image from its ID
     *
     * @param id The ID of the image as string
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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

    /**
     * Retrieve the avatar of the logged user
     *
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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

    /**
     * Retrieve all Imgur images of the logged user
     *
     * @param page The number of the requested page
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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

    /**
     * Retrieve all Imgur favorited images of the logged user
     *
     * @param page The number of the requested page
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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

    /**
     * Retrieve all Imgur images from a search query
     *
     * @param value The value of the request
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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

    /**
     * Retrieve biography of the logged user
     *
     * @param resolve Function to execute if the request success
     * @param reject Function to execute if the request fail
     */
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