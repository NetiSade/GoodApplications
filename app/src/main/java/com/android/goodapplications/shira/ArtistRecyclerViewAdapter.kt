package com.android.goodapplications.shira


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.goodapplications.shira.Model.Artist
import com.android.goodapplications.shira.databinding.RvItemArtistBinding


/**
 * Created by nsade on 13-Dec-17.
 */

class ArtistRecyclerViewAdapter(private var items: ArrayList<Artist>,
private var listener: OnItemClickListener)
: RecyclerView.Adapter<ArtistRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = RvItemArtistBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(private var binding: RvItemArtistBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist, listener:  OnItemClickListener?) {
            binding.artist = artist
            if (listener != null) {
                binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })
            }

            binding.executePendingBindings()
        }
    }

    fun replaceData(newArtists: ArrayList<Artist>) {
        items = newArtists
        notifyDataSetChanged()
    }

}