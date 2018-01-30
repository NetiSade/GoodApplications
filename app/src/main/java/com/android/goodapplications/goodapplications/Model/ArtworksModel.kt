package com.android.goodapplications.goodapplications.Model


import com.android.goodapplications.goodapplications.Model.DAL.FirebaseDAL

/**
 * Created by nsade on 10-Dec-17.
 */
class ArtworksModel {

    var firebase: FirebaseDAL = FirebaseDAL()

    fun getArtworks (onArtworksReadyCallback: OnArtworksReadyCallback) {
        firebase.listenToArtworks("artworks")
        {artworks ->
            onArtworksReadyCallback.onArtworksReady(artworks)
        }
    }

    fun searchInArtworks(onArtworksReadyCallback: OnArtworksReadyCallback, strToSearch: String){
        firebase.searchArtworks("artworks",strToSearch)
        { artworks ->
            onArtworksReadyCallback.onArtworksReady(artworks)
        }
    }
}


interface OnArtworksReadyCallback{
    fun onArtworksReady(data: ArrayList<Artwork>)
}
