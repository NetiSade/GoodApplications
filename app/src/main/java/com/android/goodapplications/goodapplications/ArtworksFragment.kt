package com.android.goodapplications.goodapplications

import android.app.Activity
import android.support.v4.app.Fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.goodapplications.goodapplications.Model.Artwork
import com.android.goodapplications.goodapplications.ViewModel.ArtworksViewModel
import com.android.goodapplications.goodapplications.databinding.FragmentArtworksBinding
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders


/**
 * Created by nsade on 30-Jan-18.
 */
class ArtworksFragment : Fragment(),ArtworkRecyclerViewAdapter.OnItemClickListener
{
    private lateinit var binding: FragmentArtworksBinding
    private val artworksRecyclerViewAdapter = ArtworkRecyclerViewAdapter(arrayListOf(),this)
    private lateinit var myActivity : Activity

    companion object {

        fun newInstance(): ArtworksFragment {
            return ArtworksFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_artworks,container,false)
        // Inflate the layout for this fragment
        val view = binding.root
        myActivity = activity
        val viewModel = ViewModelProviders.of(activity).get(ArtworksViewModel::class.java)
        binding.viewModel  = viewModel
        binding.executePendingBindings()
        binding.artworksRv.layoutManager = LinearLayoutManager(activity)
        binding.artworksRv.adapter = artworksRecyclerViewAdapter
        viewModel.artworksSearchRes.observe(this,
                Observer<ArrayList<Artwork>> { it?.let{
                    artworksRecyclerViewAdapter.replaceData(it)}
                })
        return view
    }

    override fun onItemClick(position: Int)
    {
        binding.viewModel!!.selectedArtwork = artworksRecyclerViewAdapter.items[position]
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = SingleArtworkFragment()
        fragmentTransaction.addToBackStack("ArtworksFragment")
        fragmentTransaction.hide(this@ArtworksFragment)
        fragmentTransaction.replace(R.id.root_layout, fragment,"SingleArtworkFragment")
        fragmentTransaction.commit()

    }

}