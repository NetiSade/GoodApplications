package com.android.goodapplications.shira

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.goodapplications.shira.Model.Artwork
import com.android.goodapplications.shira.ViewModel.ArtworksViewModel
import com.android.goodapplications.shira.databinding.RvItemArtworkBinding
import com.github.ivbaranov.mfb.MaterialFavoriteButton

/**
 * Created by nsade on 13-Dec-17.
 */

class ArtworkRecyclerViewAdapter(internal var items: ArrayList<Artwork>,
                                 private var listener: OnItemClickListener,
                                 var viewModel: ArtworksViewModel)
: RecyclerView.Adapter<ArtworkRecyclerViewAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = RvItemArtworkBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding,items,viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(private var binding: RvItemArtworkBinding, var items: ArrayList<Artwork>, var viewModel: ArtworksViewModel) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(artwork: Artwork, listener:  OnItemClickListener?) {
            binding.artwork = artwork
            if (listener != null) {
                binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })
            }
            binding.executePendingBindings()

            val mfb =  itemView.findViewById<MaterialFavoriteButton>(R.id.favorite_button)
            mfb.isFavorite = viewModel.isFavorite(artwork.artworkId)

            mfb?.setOnClickListener(
                    {
                        if (viewModel.getCurrentUser()!=null) {
                            if (mfb.isFavorite) {
                                mfb.isFavorite = false
                                viewModel.removeFromFavorites(artwork.artworkId)
                            } else {
                                mfb.isFavorite = true
                                viewModel.addToFavorites(artwork.artworkId)
                            }
                        }
                    }
            )
        }
    }

    fun replaceData(newArtworks: ArrayList<Artwork>) {
        items = newArtworks
        notifyDataSetChanged()
    }


}