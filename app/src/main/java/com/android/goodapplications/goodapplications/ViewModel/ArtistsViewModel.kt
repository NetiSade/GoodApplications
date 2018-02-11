package com.android.goodapplications.goodapplications.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.android.goodapplications.goodapplications.Model.*

/**
 * Created by nsade on 10-Dec-17.
 */
class ArtistsViewModel : ViewModel() {
    var model: ArtistsModel = ArtistsModel()

    val text = ObservableField("old data")

    val isLoading = ObservableField(false)

    val isSearching = ObservableField(false)

    var artists = MutableLiveData<ArrayList<Artist>>()

    var artistsSearchRes = MutableLiveData<ArrayList<Artist>>()

    var selectedArtist: Artist?= null

    fun loadArtists() {
        isLoading.set(true)
        model.getArtists(object : OnArtistsReadyCallback {
            override fun onArtistsReady(data: ArrayList<Artist>) {
                isLoading.set(false)
                artists.value = data
                resetSearchRes()
            }
        })
    }

    fun searchArtists(query : String){
        Log.d("searchArtists","Start searching for "+query)
        isSearching.set(true)
        val results = ArrayList<Artist>()
        try {
            artists.value!!.filterTo(results) { it.name.contains(query) }
            artistsSearchRes.value = results
            Log.d("searchArtists","Found "+results.size+" artists with "+query)
        }
        catch (e:Exception)
        {
            Log.d("searchArtists Exception",e.toString())
        }
        isSearching.set(false)
    }

    fun resetSearchRes()
    {
        artistsSearchRes.value = artists.value
    }

}