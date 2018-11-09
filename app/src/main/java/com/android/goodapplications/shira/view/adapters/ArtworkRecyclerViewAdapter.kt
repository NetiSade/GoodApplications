package com.android.goodapplications.shira.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.goodapplications.shira.R
import com.android.goodapplications.shira.model.Artwork
import com.android.goodapplications.shira.viewModel.ArtworksViewModel
import com.android.goodapplications.shira.databinding.RvItemArtworkBinding
import com.android.goodapplications.shira.utils.toast
import com.github.ivbaranov.mfb.MaterialFavoriteButton

/**
 * Created by nsade on 13-Dec-17.
 */

class ArtworkRecyclerViewAdapter(internal var items: ArrayList<Artwork>,
                                 private var listener: OnItemClickListener,
                                 var viewModel: ArtworksViewModel)
: RecyclerView.Adapter<ArtworkRecyclerViewAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvItemArtworkBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, items, viewModel)
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

        fun bind(artwork: Artwork?, listener:  OnItemClickListener?)
        {
            binding.artwork = artwork
            if (listener != null)
            {
                binding.root.setOnClickListener { _ -> listener.onItemClick(layoutPosition) }
            }
            binding.executePendingBindings()

            val favoriteBtn =  itemView.findViewById<MaterialFavoriteButton>(R.id.favorite_button)
            if (artwork != null)
            {
                favoriteBtn.isFavorite = viewModel.isFavorite(artwork.artworkId)
            }

            favoriteBtn?.setOnClickListener {
                val context = favoriteBtn.context
                if (viewModel.getCurrentUser()!=null)
                {
                    if (favoriteBtn.isFavorite)
                    {
                        favoriteBtn.isFavorite = false
                        if (artwork != null)
                        {
                            viewModel.removeFromFavorites(artwork.artworkId)
                            context.toast(artwork.title +" "+context.getString(R.string.fav_btn_msg_removed_from_fav))
                        }
                    } else
                    {
                        favoriteBtn.isFavorite = true
                        if (artwork != null)
                        {
                            viewModel.addToFavorites(artwork.artworkId)
                            context.toast(artwork.title+" "+context.getString(R.string.fav_btn_msg_added_to_fav))
                        }
                    }
                }
                else
                {
                    favoriteBtn.context.toast(R.string.fav_btn_msg_logedout)
                }
            }
        }
    }

    fun replaceData(newArtworks: ArrayList<Artwork>) {
        items = newArtworks
        notifyDataSetChanged()
    }


}