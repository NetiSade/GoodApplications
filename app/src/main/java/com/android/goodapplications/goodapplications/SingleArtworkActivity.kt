package com.android.goodapplications.goodapplications


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.goodapplications.goodapplications.ViewModel.ArtworksViewModel
import com.android.goodapplications.goodapplications.databinding.ActivitySingleArtworkBinding
import kotlinx.android.synthetic.main.app_bar_main.*
import android.text.method.ScrollingMovementMethod
import android.widget.TextView


class SingleArtworkActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySingleArtworkBinding
    lateinit var viewModel:ArtworksViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_artwork)
        viewModel = ViewModelProviders.of(this).get(ArtworksViewModel::class.java)
        val intent = intent
        val artworkTitle = intent.getStringExtra("title")
        val artworkArtistName = intent.getStringExtra("artistName")
        val artworkBodyText = intent.getStringExtra("bodyText")+"\n\n"
        val titleTextView: TextView = findViewById(R.id.single_artwork_title)
        val artistNameTextView: TextView = findViewById(R.id.single_artwork_artistName)
        val artworkBodyTextView: TextView = findViewById(R.id.single_artwork_bodyText)
        titleTextView.text=artworkTitle
        artistNameTextView.text=artworkArtistName
        artworkBodyTextView.text=artworkBodyText
        artworkBodyTextView.movementMethod = ScrollingMovementMethod()
        var selectedArtwork = viewModel.selectedArtwork//TODO: selectedArtwork is null now!
        binding.artwork = selectedArtwork
        binding.executePendingBindings()


    }



}

