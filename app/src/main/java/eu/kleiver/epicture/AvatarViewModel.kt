package eu.kleiver.epicture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * View model that implement a MutableLiveData of the avatar url (to observe it).
 */
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

/**
 * View model that implement a MutableLiveData of the user biography (to observe it).
 */
class BioViewModel : ViewModel() {
    val url: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun loadBio() {
        ImgurAPI.getUserBio({ receivedData ->
            url.postValue(receivedData.getJSONObject("data").getString("bio"))
        }, {
            url.postValue(null)
        })
    }
}