package com.android.goodapplications.goodapplications


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.android.goodapplications.goodapplications.Model.Artist
import com.android.goodapplications.goodapplications.ViewModel.ArtistsViewModel
import com.android.goodapplications.goodapplications.databinding.ActivityArtistsBinding
import kotlinx.android.synthetic.main.app_bar_main.*


class ArtistsActivity : AppCompatActivity(), ArtistRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: ActivityArtistsBinding
    private val artistRecyclerViewAdapter = ArtistRecyclerViewAdapter(arrayListOf(),this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_artists)
        val viewModel = ViewModelProviders.of(this).get(ArtistsViewModel::class.java)
        viewModel.loadArtists()
        binding.viewModel = viewModel
        binding.executePendingBindings()
        binding.artistsRv.layoutManager = LinearLayoutManager(this)
        binding.artistsRv.adapter = artistRecyclerViewAdapter
        viewModel.artists.observe(this,
                  Observer<ArrayList<Artist>> { it?.let{
                      artistRecyclerViewAdapter.replaceData(it)}
                  })
    }


    override fun onItemClick(position: Int) {

    }
}
