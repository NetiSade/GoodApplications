package com.android.goodapplications.shira.model

/**
 * Created by nsade on 10-Dec-17.
 */
class ArtistsModel :  GoodApplicationModel() {

    fun getArtists(onArtistsReadyCallback: OnArtistsReadyCallback) {
        firebaseDAL.listenToArtists()
        {artists ->
            onArtistsReadyCallback.onArtistsReady(artists)
        }
    }
}

interface OnArtistsReadyCallback{
    fun onArtistsReady(data: ArrayList<Artist>)
}
