package com.android.goodapplications.shira.model.dal

import android.content.ContentValues
import android.databinding.ObservableArrayList
import android.util.Log
import com.android.goodapplications.shira.model.Artist
import com.android.goodapplications.shira.model.Artwork
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

/**
 * Created by nsade on 14-Dec-17.
 */
class FirebaseDAL : AbstractDAL() {
    private val dbRef = FirebaseFirestore.getInstance()
    var listener: ListenerRegistration? = null
    private var mAuth: FirebaseAuth? = null
    private val artworksCollectionRef = "artworks"
    private val artistsCollectionRef = "artists"

    init {
        getDBOffline()
        mAuth = FirebaseAuth.getInstance()
    }

    private fun getDBOffline() {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        dbRef.firestoreSettings = settings
    }

    fun listenToArtists(handler: (ArrayList<Artist>) -> Unit) {
        listener = dbRef.collection(artistsCollectionRef).orderBy("name")
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
                            try {
                                artistsArray.add(document.toObject(Artist::class.java)!!)
                            } catch (e: Exception) {
                                Log.d(ContentValues.TAG, "Cannot create Artist obj from " + document.toString())
                                Log.d(ContentValues.TAG, e.toString())
                            }
                        }
                    }
                    handler(artistsArray)
                })
    }

    fun listenToArtworks(handler: (ArrayList<Artwork>) -> Unit) {
        listener = dbRef.collection(artworksCollectionRef).orderBy("publisheDate",Query.Direction.DESCENDING)
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null)
                    {
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
                            Log.d(ContentValues.TAG, "Data fetched from $source")
                            try {
                                val artwork = document.toObject(Artwork::class.java)
                                if (artwork != null) {
                                    val fixedArtwork = fixArtwork(artwork,document)
                                    artworksArray.add(fixedArtwork)
                                }
                            } catch (e: Exception) {
                                Log.d(ContentValues.TAG, "Cannot create Artwork obj document: " + document.toString())
                                Log.d(ContentValues.TAG, e.toString())
                            }
                        }
                    }
                    handler(artworksArray)
                })
    }

    private fun fixArtwork(artwork: Artwork, document: DocumentSnapshot?): Artwork
    {
        if (document != null)
        {
            artwork.artworkId = document.reference.id
            artwork.createdAt = document.getDate("createdAt")
            artwork.publisheDate = document.getDate("publisheDate")
            artwork.updatedAt = document.getDate("updatedAt")
        }
        return artwork
    }

    fun listenToFavoritesArtworks(handler: (ObservableArrayList<String>) -> Unit)
    {
        val favArtworksArray: ObservableArrayList<String> = ObservableArrayList()
        if (getCurrentUser() != null) {
            listener = dbRef.collection("users").document(getCurrentUser()!!.uid)
                    .collection("favorites_artworks")
                    .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
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

    fun addArtworkToFavList(artworkId: String) {
        if (getCurrentUser() != null) {
            val map: HashMap<String, Any> = HashMap()
            map[artworkId] = ""
            val user = getCurrentUser()!!.uid
            dbRef.collection("users").document(user)
                    .collection("favorites_artworks")
                    .document(artworkId).set(map)
        }
    }

    fun removeArtworkFromFavList(artworkId: String) {
        if (getCurrentUser() != null) {
            dbRef.collection("users")
                    .document(getCurrentUser()!!.uid)
                    .collection("favorites_artworks")
                    .document(artworkId).delete()
        }
    }

    fun isThereArtworkForToday(handler: (Artwork?) -> Unit)
    {
        var found = false
        try {
            val calender = Calendar.getInstance()
            calender.add(Calendar.DAY_OF_YEAR, -2)
            calender.set(Calendar.HOUR, 23)
            calender.set(Calendar.MINUTE, 59)
            calender.set(Calendar.SECOND, 59)
            val today = calender.time
            calender.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = calender.time
            listener = dbRef.collection(artworksCollectionRef)
                    .orderBy("publisheDate",Query.Direction.DESCENDING)
                    .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                        if (e != null)
                        {
                            Log.w(ContentValues.TAG, "Listen error", e)
                            return@EventListener
                        }
                        if (querySnapshot != null)
                        {
                            for (document in querySnapshot.documents)
                            {

                                try {
                                    val artDate = document.getDate("publisheDate")
                                    if(artDate!!.before(today))
                                    {
                                        handler(null)
                                        break
                                    }
                                    if(artDate.after(today) && artDate.before(tomorrow))
                                    {
                                        val artwork = document.toObject(Artwork::class.java)
                                        if (artwork != null)
                                        {
                                                found = true
                                                handler(fixArtwork(artwork,document))
                                                break
                                        }
                                    }

                                } catch (e: Exception)
                                {
                                    Log.d(ContentValues.TAG, "Cannot create Artwork obj document: " + document.toString())
                                    Log.d(ContentValues.TAG, e.toString())
                                }
                            }
                        }
                        if(!found) {
                            handler(null)
                        }
                    })
        }
        catch (e: Exception)
        {
            Log.d("IsThereArtworkForToday", e.message)
            handler(null)
        }
    }

    fun getArtwork(handler: (Artwork?) -> Unit, artworkId: String) = try {
        listener = dbRef.collection(artworksCollectionRef).whereEqualTo(FieldPath.documentId(),artworkId)
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return@EventListener
                    }
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents)
                        {
                            try {
                                if(document.reference.id == artworkId) {
                                    val artwork = document.toObject(Artwork::class.java)
                                    if (artwork != null)
                                    {
                                        handler(fixArtwork(artwork,document))
                                    }
                                }
                            } catch (e: Exception) {
                                Log.d(ContentValues.TAG, "Cannot create Artwork obj document: " + document.toString())
                                Log.d(ContentValues.TAG, e.toString())
                            }
                        }
                    }
                })
    } catch (e: Exception) {
        Log.d("IsThereArtworkForToday", e.message)
        handler(null)
    }


}




