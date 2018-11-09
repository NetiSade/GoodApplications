package com.android.goodapplications.shira.view.fragments

import android.support.v4.app.Fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.goodapplications.shira.model.Artwork
import com.android.goodapplications.shira.databinding.FragmentArtworksBinding
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.android.goodapplications.shira.R
import com.android.goodapplications.shira.view.adapters.ArtworkRecyclerViewAdapter
import com.android.goodapplications.shira.viewModel.ArtworksViewModel

/**
 * Created by nsade on 30-Jan-18.
 */
class ArtworksFragment : Fragment(), ArtworkRecyclerViewAdapter.OnItemClickListener
{

    private lateinit var binding: FragmentArtworksBinding
    private lateinit var artworksRecyclerViewAdapter : ArtworkRecyclerViewAdapter
    var viewModel : ArtworksViewModel? = null

    companion object {

        fun newInstance(): ArtworksFragment {
            return ArtworksFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artworks,container,false)
        // Inflate the layout for this fragment
        val view = binding.root
        viewModel = ViewModelProviders.of(activity!!).get(ArtworksViewModel::class.java)
        artworksRecyclerViewAdapter = ArtworkRecyclerViewAdapter(arrayListOf(), this, viewModel!!)
        binding.viewModel  = this.viewModel
        binding.executePendingBindings()
        binding.artworksRv.layoutManager = LinearLayoutManager(activity)
        binding.artworksRv.adapter = artworksRecyclerViewAdapter
        viewModel!!.artworksSearchRes.observe(this,
                Observer<ArrayList<Artwork>> { it?.let{
                    artworksRecyclerViewAdapter.replaceData(it)}
                })
        return view
    }

    override fun onItemClick(position: Int)
    {
       // binding.viewModel.selectedArtwork = artworksRecyclerViewAdapter.items[position]
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val fragment = SingleArtworkFragment()
        fragmentTransaction.addToBackStack("ArtworksFragment")
        fragmentTransaction.hide(this@ArtworksFragment)
        fragmentTransaction.replace(R.id.root_layout, fragment,"SingleArtworkFragment")
        fragmentTransaction.commit()
    }

}