package com.android.goodapplications.goodapplications


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import com.android.goodapplications.goodapplications.ViewModel.ArtworksViewModel
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.goodapplications.goodapplications.databinding.FragmentSingleArtworkBinding


class SingleArtworkFragment : Fragment(){

    private lateinit var binding: FragmentSingleArtworkBinding

    companion object {

        fun newInstance(): ArtworksFragment {
            return ArtworksFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_artwork,container,false)
        val view = binding.root
        val activity = activity
        val artwork = ViewModelProviders.of(activity).get(ArtworksViewModel::class.java).selectedArtwork
        binding.artwork = artwork
        binding.executePendingBindings()
        return view
    }


}

