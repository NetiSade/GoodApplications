package com.android.goodapplications.goodapplications.Model.DAL

import android.content.ContentValues
import android.util.Log
import com.android.goodapplications.goodapplications.Model.Artist
import com.android.goodapplications.goodapplications.Model.Artwork
import com.google.firebase.firestore.*
/**
 * Created by nsade on 14-Dec-17.
 */
class FirebaseDAL : AbstractDAL()
{
    private val dbRef = FirebaseFirestore.getInstance()
    var listener :ListenerRegistration? = null

    init
    {
        getDBOffline()
    }

    private fun getDBOffline()
    {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        dbRef.firestoreSettings = settings
    }

    fun listenToArtists(collectionRef:String, handler: (ArrayList<Artist>) -> Unit)
    {
        listener= dbRef.collection(collectionRef).orderBy("name")
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return@EventListener
                    }
                    val artistsArray: ArrayList<Artist> = ArrayList()
                    for (document in querySnapshot.documents)
                    {
                        val source = if (querySnapshot.metadata.isFromCache)
                            "local cache"
                        else
                            "server"
                        Log.d(ContentValues.TAG, "Data fetched from " + source)
                        try{
                        artistsArray.add(document.toObject(Artist::class.java))}
                        catch (e:Exception)
                        {
                            Log.d(ContentValues.TAG, "Cannot create Artist obj from "+document.toString())
                            Log.d(ContentValues.TAG, e.toString())
                        }
                    }
                    handler(artistsArray)
                })
    }


    fun listenToArtworks(collectionRef:String, handler: (ArrayList<Artwork>) -> Unit)
    {
        listener= dbRef.collection(collectionRef).orderBy("publisheDate")
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return@EventListener
                    }
                    val artworksArray: ArrayList<Artwork> = ArrayList()
                    for (document in querySnapshot.documents)
                    {
                        val source = if (querySnapshot.metadata.isFromCache)
                            "local cache"
                        else
                            "server"
                        Log.d(ContentValues.TAG, "Data fetched from " + source)
                        try {
                            artworksArray.add(document.toObject(Artwork::class.java))
                        }
                        catch (e: Exception) {
                            Log.d(ContentValues.TAG, "Cannot create Artwork obj document: "+document.toString())
                            Log.d(ContentValues.TAG, e.toString())
                         }
                    }
                    handler(artworksArray)
                })
    }

    fun searchArtworks(collectionRef:String, strToSearch: String,  handler: (ArrayList<Artwork>) -> Unit){
        listener= dbRef.collection(collectionRef)
                .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return@EventListener
                    }
                    val artworksArray: ArrayList<Artwork> = ArrayList()
                    for (document in querySnapshot.documents)
                    {
                        var artwork: Artwork
                        if(document.contains(strToSearch)) {
                            try {
                                artwork = document.toObject(Artwork::class.java)
                                artworksArray.add(artwork)
                                Log.d("ContentValues.TAG","SearchRes: word: "+strToSearch+" "+ artwork.title)
                            } catch (e: Exception) {
                                Log.d(ContentValues.TAG, "Cannot create Artwork obj document: " + document.toString())
                                Log.d(ContentValues.TAG, e.toString())
                            }
                        }
                    }
                    handler(artworksArray)
                })
    }
}




