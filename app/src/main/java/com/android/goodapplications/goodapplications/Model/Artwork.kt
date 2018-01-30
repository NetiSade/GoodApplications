package com.android.goodapplications.goodapplications.Model

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import java.util.Date

/**
 * Created by nsade on 15-Nov-17.
 */
class Artwork(val acum: Boolean = false,
              val artistName: String = "",
              val bodyText: String = "",
              val createdAt: Date = Date(),
              val createdBy: String = "",
              val dedication: String = "",
              val description: String = "",
              val image: String = "",
              val isPublic: Boolean = false,
              val link: String = "",
              val note: String = "",
              val publisheDate: Date = Date(),
              val secondaryImage: String = "",
              val source: String = "",
              val sourceLang: String = "",
              val sourceLangBodyText: String = "",
              val strippedBodyText: String = "",
              val strippedTitle: String = "",
              val title: String = "",
              val type: String = "",
              //val updatedAt: Date = Date(),
              val verifiedByEditor: Boolean = false,
              val year: String = ""
             // val artistRef: DocumentReference
) {
    init
    {
        Log.d("Artwork","New Artwork created!")
    }
}