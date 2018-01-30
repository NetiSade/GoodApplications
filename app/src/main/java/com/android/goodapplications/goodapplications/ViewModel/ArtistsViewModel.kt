package com.android.goodapplications.goodapplications.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.arch.lifecycle.ViewModel
import com.android.goodapplications.goodapplications.Model.*

/**
 * Created by nsade on 10-Dec-17.
 */
class ArtistsViewModel : ViewModel() {
    var model: ArtistsModel = ArtistsModel()

    val text = ObservableField("old data")

    val isLoading = ObservableField(false)

    var artists = MutableLiveData<ArrayList<Artist>>()

    var selectedArtist: Artist?= null

    fun loadArtists() {
        isLoading.set(true)
        model.getArtists(object : OnArtistsReadyCallback {
            override fun onArtistsReady(data: ArrayList<Artist>) {
                isLoading.set(false)
                artists.value = data
            }
        })
    }

}