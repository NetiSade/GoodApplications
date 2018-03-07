package com.android.goodapplications.shira.Model

import android.databinding.ObservableArrayList


/**
 * Created by nsade on 10-Dec-17.
 */
class ArtworksModel : GoodApplicationModel() {

    fun getArtworks (onArtworksReadyCallback: OnArtworksReadyCallback) {
        firebaseDAL.listenToArtworks()
        {artworks ->
            onArtworksReadyCallback.onArtworksReady(artworks)
        }
    }

    fun loadFavArtworks (onFavListReady: OnFavListReadyCallback){
        firebaseDAL.listenToFavoritesArtworks()
        {it ->
            onFavListReady.onFavIdListReady(it)
        }
    }
    fun addToArtworksFavList(artworkId: String)
    {
        firebaseDAL.addArtworkToFavList(artworkId)
    }

    fun removeFromArtworksFavList (artworkId: String)
    {
        firebaseDAL.removeArtworkFromFavList(artworkId)
    }
}

interface OnArtworksReadyCallback{
    fun onArtworksReady(data: ArrayList<Artwork>)
}

interface OnFavListReadyCallback{
    fun onFavIdListReady(data: ObservableArrayList<String>)
}
