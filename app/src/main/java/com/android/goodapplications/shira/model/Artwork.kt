package com.android.goodapplications.shira.model

import android.text.SpannableStringBuilder
import android.util.Log
import java.util.Date

/**
 * Created by nsade on 15-Nov-17.
 */
data class Artwork(val acum: Boolean = false,
              var artistName:  String = "",
              val bodyText: String = "",
              val createdAt: Date = Date(),
              val createdBy: String = "",
              val dedication: String = "",
              val description: String = "",
              val image: String = "",
              val isPublic: Boolean = false,
              val link: String = "",
              val note: String = "",
              val publishedDate: Date = Date(),
              val secondaryImage: String = "",
              val source: String = "",
              val sourceLang: String = "",
              val sourceLangBodyText: String = "",
              val strippedBodyText: String = "",
              val strippedTitle: String = "",
              var title: String = "",
              val type: String = "",
              val updatedAt: Date = Date(),
              val verifiedByEditor: Boolean = false,
              val year: String = "",
              var SpannableStringTitle : SpannableStringBuilder = SpannableStringBuilder (""),
              var SpannableStringArtistName : SpannableStringBuilder = SpannableStringBuilder (""),
              var searchRes: SpannableStringBuilder = SpannableStringBuilder (""),
              var showSearchRes: Boolean = false,
              var favorite : Boolean = false,
              var artworkId: String = ""
              //val artistRef: DocumentReference

) {
    init
    {
        Log.d("Artwork","New Artwork created!")
    }

}
