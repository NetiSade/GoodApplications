package com.android.goodapplications.goodapplications


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.goodapplications.goodapplications.Model.Artwork
import com.android.goodapplications.goodapplications.databinding.RvItemArtworkBinding


/**
 * Created by nsade on 13-Dec-17.
 */

class ArtworkRecyclerViewAdapter(internal var items: ArrayList<Artwork>,
                                 private var listener: OnItemClickListener)
: RecyclerView.Adapter<ArtworkRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = RvItemArtworkBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(private var binding: RvItemArtworkBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(artwork: Artwork, listener:  OnItemClickListener?) {
            binding.artwork = artwork
            if (listener != null) {
                binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })
            }
            binding.executePendingBindings()
        }
    }

    fun replaceData(newArtworks: ArrayList<Artwork>) {
        items = newArtworks
        notifyDataSetChanged()
    }

    fun searchData(artworks: ArrayList<Artwork>, atrToSearch: String) {
        val newItems = ArrayList<Artwork>()
        artworks.filterTo(newItems) { it.bodyText.contains(atrToSearch) }
        replaceData(newItems)
    }

}