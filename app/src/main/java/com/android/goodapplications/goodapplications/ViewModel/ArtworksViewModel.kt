package com.android.goodapplications.goodapplications.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.android.goodapplications.goodapplications.Model.*
import android.text.Spannable
import android.text.SpannableStringBuilder
import kotlin.math.max


/**
 * Created by nsade on 10-Dec-17.
 */
class ArtworksViewModel : ViewModel() {

    var model: ArtworksModel = ArtworksModel()

    val text = ObservableField("old data")

    val isLoading = ObservableField(false)

    val isSearching = ObservableField(false)

    var artworks = MutableLiveData<ArrayList<Artwork>>()

    var artworksSearchRes = MutableLiveData<ArrayList<Artwork>>()

    var selectedArtwork : Artwork? = null

    val WORDS_TO_DISPLAY: Int = 4

    fun loadArtworks() {
        isLoading.set(true)
        model.getArtworks(object : OnArtworksReadyCallback{
            override fun onArtworksReady(data: ArrayList<Artwork>) {
                artworks.value = data
                resetSearchRes()
                isLoading.set(false)
            }
        })
    }

    fun searchArtworks(query : String){
        isSearching.set(true)
        resetSearchRes()
        Log.d("searchArtworks","Start searching for "+query)
        val results = ArrayList<Artwork>()
        for(artwork:Artwork in artworks.value!!)
        {
            try{
                var add = false
                val tempArtwork = artwork.copy()
                val bodyWithoutEndOfLines = removeEndOfLine(tempArtwork.strippedBodyText)
                if(bodyWithoutEndOfLines.contains(query))
                {
                    markQueryInBody(tempArtwork, bodyWithoutEndOfLines, query)
                    add = true
                }
                if(artwork.title.contains(query))
                {
                    markQueryInLine(tempArtwork.SpannableStringTitle,query)
                    add = true
                }
                if(artwork.artistName.contains(query))
                {
                    markQueryInLine(tempArtwork.SpannableStringArtistName,query)
                    add = true
                }
                if(add) results.add(tempArtwork)
            }
            catch (e:Exception)
            {
                Log.d("searchArtworks",e.toString())
                isSearching.set(false)
            }
        }
        artworksSearchRes.value = results
        isSearching.set(false)
    }

    private fun getStrToShow(bodyText: String, query: String): String
    {
        val ind = bodyText.indexOf(query)
        val startWords = bodyText.substring(0,ind).split(' ','\n')
        val endWords = bodyText.substring(ind,bodyText.length).split(' ','\n')
        var newStr = "\""

        var i = max (startWords.size-WORDS_TO_DISPLAY , 0)
        if(i!=0)
            newStr+="..."
        while(i < startWords.size)
        {
            if(startWords[i].contains("."))
                newStr="..."
            else
                newStr+=startWords[i]
            i++
            if(i<startWords.size)
                newStr+=" "
        }

        i = 0
        while(i < WORDS_TO_DISPLAY && i < endWords.size)
        {
            newStr+=endWords[i]
            if(endWords[i].contains("."))
            {
                i++
                break
            }
            i++
            if(i < WORDS_TO_DISPLAY && i < endWords.size)
                newStr+=" "
        }
        if(i!=endWords.size)
            newStr+="..."
        newStr+="\""
        return newStr
    }

    fun resetSearchRes()
    {
        artworksSearchRes.value = artworks.value
        for(artwork:Artwork in artworksSearchRes.value!!)
        {
            artwork.SpannableStringArtistName = SpannableStringBuilder(artwork.artistName)
            artwork.SpannableStringTitle = SpannableStringBuilder(artwork.title)
        }

    }

    private fun markQueryInBody(tempArtwork:Artwork,bodyWithoutEndOfLines: String, query:String)
    {
        val ind = bodyWithoutEndOfLines.indexOf(query)
        if(ind >= 0)
        {
            val strToShow = getStrToShow(bodyWithoutEndOfLines,query)
            val spannableStrToShow = SpannableStringBuilder(strToShow)
            val newInd = spannableStrToShow.indexOf(query)
            spannableStrToShow.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), newInd, newInd+query.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tempArtwork.searchRes = spannableStrToShow
        }
        tempArtwork.showSearchRes = true

    }

    private fun markQueryInLine (line: SpannableStringBuilder, query: String)
    {
        val ind = line.toString().indexOf(query)
        line.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), ind, ind+query.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun removeEndOfLine(text:String) : String
    {
        val newStr = text.replace("\n"," ")
        newStr.replace("\t"," ")
        return newStr
    }

}