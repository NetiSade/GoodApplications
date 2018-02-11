package com.android.goodapplications.goodapplications


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.goodapplications.goodapplications.Model.Artist
import com.android.goodapplications.goodapplications.ViewModel.ArtistsViewModel
import com.android.goodapplications.goodapplications.databinding.FragmentArtistsBinding


class ArtistsFragment : Fragment(), ArtistRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: FragmentArtistsBinding
    private val artistRecyclerViewAdapter = ArtistRecyclerViewAdapter(arrayListOf(),this)

    companion object {
        fun newInstance(): ArtistsFragment {
            return ArtistsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_artists,container,false)
        // Inflate the layout for this fragment
        val view = binding.root
        val activity = activity
        val viewModel = ViewModelProviders.of(activity).get(ArtistsViewModel::class.java)
        binding.viewModel  = viewModel
        binding.executePendingBindings()
        binding.artistsRv.layoutManager = LinearLayoutManager(activity)
        binding.artistsRv.adapter = artistRecyclerViewAdapter
        viewModel.artistsSearchRes.observe(this,
                Observer<ArrayList<Artist>> { it?.let{
                    artistRecyclerViewAdapter.replaceData(it)}
                })
        return view
    }


    override fun onItemClick(position: Int) {

    }
}
