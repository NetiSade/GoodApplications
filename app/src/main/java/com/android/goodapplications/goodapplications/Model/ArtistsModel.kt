package com.android.goodapplications.goodapplications.Model


import com.android.goodapplications.goodapplications.Model.DAL.FirebaseDAL

/**
 * Created by nsade on 10-Dec-17.
 */
class ArtistsModel {

    var firebase: FirebaseDAL = FirebaseDAL()

    fun getArtists(onArtistsReadyCallback: OnArtistsReadyCallback) {
        firebase.listenToArtists("artists")
        {artists ->
            onArtistsReadyCallback.onArtistsReady(artists)
        }
    }

}

interface OnArtistsReadyCallback{
    fun onArtistsReady(data: ArrayList<Artist>)
}
