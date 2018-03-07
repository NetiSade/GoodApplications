package com.android.goodapplications.shira.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.util.Log
import com.android.goodapplications.shira.Model.*
import android.text.Spannable
import android.text.SpannableStringBuilder
import com.google.firebase.auth.FirebaseUser
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

    var favArtworksIdList = MutableLiveData<ObservableArrayList<String>>()

    var favArtworkList = MutableLiveData<ArrayList<Artwork>>()

    val WORDS_TO_DISPLAY: Int = 4

    fun loadData()
    {
        favArtworksIdList.value = ObservableArrayList()
        artworks.value = ArrayList()
        favArtworkList.value = ArrayList()
        loadArtworks()
    }

    private fun addOnChangeListener() {//TODO: Check it! its didn't work
        favArtworksIdList.value!!.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<String>>(){
            override fun onItemRangeRemoved(p0: ObservableList<String>?, p1: Int, p2: Int) {}
            override fun onItemRangeMoved(p0: ObservableList<String>?, p1: Int, p2: Int, p3: Int) {}
            override fun onItemRangeInserted(p0: ObservableList<String>?, p1: Int, p2: Int) {}
            override fun onItemRangeChanged(p0: ObservableList<String>?, p1: Int, p2: Int) {}
            override fun onChanged(p0: ObservableList<String>?) {
                markAndSaveFavArtworks()
            }

        })
    }

    fun loadArtworks() {
        isLoading.set(true)
        model.getArtworks(object : OnArtworksReadyCallback{
            override fun onArtworksReady(data: ArrayList<Artwork>) {
                artworks.value = data
                resetSearchRes()
                loadFavArtworksId()
            }
        })
    }

    fun loadFavArtworksId(){
        model.loadFavArtworks(object : OnFavListReadyCallback{
            override fun onFavIdListReady(data: ObservableArrayList<String>) {
                favArtworksIdList.value = data
                markAndSaveFavArtworks()
                isLoading.set(false)
            }
        })
    }

    private fun markAndSaveFavArtworks() {
        favArtworkList.value = ArrayList()
        if (favArtworksIdList.value!=null)
            for(artworkId in favArtworksIdList.value!!) {
                val artwork = findByArtworksId(artworkId)
                if(artwork!=null) {
                    artwork.favorite = true
                    favArtworkList.value!!.add(artwork)
                }
            }
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

    fun addToFavorites ( ref : String)
    {
        if(model.getCurrentUser()!=null) {
            if (ref !in favArtworksIdList.value!!) {
                favArtworksIdList.value!!.add(ref)
                model.addToArtworksFavList(ref)
            }
            val artwork = findByArtworksId(ref)
            if (artwork!=null && favArtworkList.value!=null){
                artwork.favorite = true
                favArtworkList.value!!.add(artwork)
            }
        }
    }

    fun removeFromFavorites ( ref : String)
    {
        if(model.getCurrentUser()!=null) {
            favArtworksIdList.value!!.remove(ref)
            val artwork = findByArtworksId(ref)
            artwork!!.favorite = false
            removeFromFavList(ref)
            model.removeFromArtworksFavList(ref)
        }
    }

    private fun removeFromFavList(ref: String) {
        if(null != favArtworkList.value)
        {
            for(artwork in favArtworkList.value!!)
            {
                if (artwork.artworkId == ref) {
                    favArtworkList.value!!.remove(artwork)
                    break
                }
            }
        }
    }

    fun isFavorite ( ref : String) : Boolean
    {
        if(favArtworksIdList.value == null)
            return false
        return (ref in favArtworksIdList.value!!)
    }

    private fun findByArtworksId (artworksId: String) : Artwork?
    {
        for (artwork in artworks.value!!)
            if (artwork.artworkId == artworksId)
                return artwork
        return null
    }

    fun getCurrentUser(): FirebaseUser? {
        return model.getCurrentUser()
    }
}
