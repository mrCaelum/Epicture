package eu.kleiver.epicture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvatarViewModel : ViewModel() {
    val url: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun loadAvatar() {
        ImgurAPI.getUserAvatar({ receivedData ->
            url.postValue(receivedData.getJSONObject("data").getString("avatar"))
        }, {
            url.postValue(null)
        })
    }
}