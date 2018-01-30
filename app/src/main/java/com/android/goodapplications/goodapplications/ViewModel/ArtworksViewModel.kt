package com.android.goodapplications.goodapplications.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.android.goodapplications.goodapplications.Model.*

/**
 * Created by nsade on 10-Dec-17.
 */
class ArtworksViewModel : ViewModel() {
    var model: ArtworksModel = ArtworksModel()

    val text = ObservableField("old data")

    val isLoading = ObservableField(false)

    val isSearching = ObservableField(false)

    var artworks = MutableLiveData<ArrayList<Artwork>>()

    var searchResults = MutableLiveData<ArrayList<Artwork>>()

    var selectedArtwork : Artwork? = null

    fun loadArtworks() {
        isLoading.set(true)
        model.getArtworks(object : OnArtworksReadyCallback{
            override fun onArtworksReady(data: ArrayList<Artwork>) {
                isLoading.set(false)
                artworks.value = data
            }
        })
    }

    fun searchArtworks(){

    }

    override fun onCleared() {
        Log.d("ViewModel","onCleared***************")
    }
}