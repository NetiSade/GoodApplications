package com.android.goodapplications.shira.model

import android.text.SpannableStringBuilder
import java.util.Date

/**
 * Created by nsade on 15-Nov-17.
 */
data class Artwork(
        var acum: Boolean = false,
        var artistName:  String = "",
        var bodyText: String = "",
        var createdAt: Date? = null,
        var createdBy: String = "",
        var dedication: String = "",
        var description: String = "",
        var image: String = "",
        var isPublic: Boolean = false,
        var link: String = "",
        var note: String = "",
        var publisheDate: Date? = null,
        var secondaryImage: String = "",
        var source: String = "",
        var sourceLang: String = "",
        var sourceLangBodyText: String = "",
        var strippedBodyText: String = "",
        var strippedTitle: String = "",
        var title: String = "",
        var type: String = "",
        var updatedAt: Date? = null,
        var verifiedByEditor: Boolean = false,
        var year: String = "",
        var SpannableStringTitle: SpannableStringBuilder = SpannableStringBuilder (""),
        var SpannableStringArtistName: SpannableStringBuilder = SpannableStringBuilder (""),
        var searchRes: SpannableStringBuilder = SpannableStringBuilder (""),
        var showSearchRes: Boolean = false,
        var favorite: Boolean = false,
        var artworkId: String = ""
              //val artistRef: DocumentReference

) {
    constructor(
            title:String,
            artistName: String,
            bodyText: String) : this(){
        this.title = title
        this.artistName = artistName
        this.bodyText = bodyText
    }

}
