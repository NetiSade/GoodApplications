package com.android.goodapplications.shira.model.dal

import android.content.ContentValues
import android.databinding.ObservableArrayList
import android.util.Log
import com.android.goodapplications.shira.model.Artist
import com.android.goodapplications.shira.model.Artwork
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import java.util.HashMap

/**
 * Created by nsade on 14-Dec-17.
 */
class FirebaseDAL : AbstractDAL()
{
    private val dbRef = FirebaseFirestore.getInstance()
    var listener :ListenerRegistration? = null
    private var mAuth: FirebaseAuth? = null
    private val artworksCollectionRef = "artworks"
    private val artistsCollectionRef = "artists"

    init
    {
        getDBOffline()
        mAuth = FirebaseAuth.getInstance()
    }

    private fun getDBOffline()
    {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        dbRef.firestoreSettings = settings
    }

    fun listenToArtists(handler: (ArrayList<Artist>) -> Unit)
    {
        listener= dbRef.collection(artistsCollectionRef).orderBy("name")
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return@EventListener
                    }
                    val artistsArray: ArrayList<Artist> = ArrayList()
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents) {
                            val source = if (querySnapshot.metadata.isFromCache)
                                "local cache"
                            else
                                "server"
                            Log.d(ContentValues.TAG, "Data fetched from $source")
                            try{
                                artistsArray.add(document.toObject(Artist::class.java)!!)} catch (e:Exception) {
                                Log.d(ContentValues.TAG, "Cannot create Artist obj from "+document.toString())
                                Log.d(ContentValues.TAG, e.toString())
                            }
                        }
                    }
                    handler(artistsArray)
                })
    }

    fun listenToArtworks(handler: (ArrayList<Artwork>) -> Unit)
    {
        listener= dbRef.collection(artworksCollectionRef).orderBy("publisheDate")
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return@EventListener
                    }
                    val artworksArray: ArrayList<Artwork> = ArrayList()
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents) {
                            val source = if (querySnapshot.metadata.isFromCache)
                                "local cache"
                            else
                                "server"
                            Log.d(ContentValues.TAG, "Data fetched from " + source)
                            try {
                                val artwork = document.toObject(Artwork::class.java)
                                if (artwork != null) {
                                    artwork.artworkId = document.reference.id
                                    artworksArray.add(artwork)
                                }
                            } catch (e: Exception) {
                                Log.d(ContentValues.TAG, "Cannot create Artwork obj document: "+document.toString())
                                Log.d(ContentValues.TAG, e.toString())
                            }
                        }
                    }
                    handler(artworksArray)
                })
    }

    fun listenToFavoritesArtworks(handler: (ObservableArrayList<String>) -> Unit)
    {
        val favArtworksArray: ObservableArrayList<String> = ObservableArrayList()
        if(getCurrentUser() != null) {
            listener = dbRef.collection("users").document(getCurrentUser()!!.uid).collection("favorites_artworks").addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "listenToFavoritesArtworks error", e)
                    return@EventListener
                }
                querySnapshot?.documents?.mapTo(favArtworksArray) { it.id }
            })
        }
        handler(favArtworksArray)
    }

    fun getCurrentUser(): FirebaseUser? {
        return mAuth!!.currentUser
    }

    fun getFirebaseAuth(): FirebaseAuth? {
        return mAuth
    }

    fun addArtworkToFavList(artworkId : String)
    {
        if (getCurrentUser()!=null) {
            val map: HashMap<String,Any> = HashMap()
            map[artworkId] = ""
            val user = getCurrentUser()!!.uid
            dbRef.collection("users").document(user).collection("favorites_artworks")
                    .document(artworkId).set(map)
        }
    }

    fun removeArtworkFromFavList(artworkId : String)
    {
        if (getCurrentUser()!=null) {
            dbRef.collection("users").document(getCurrentUser()!!.uid).collection("favorites_artworks")
                    .document(artworkId).delete()
        }
    }


}




