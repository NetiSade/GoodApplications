package com.android.goodapplications.goodapplications



import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.android.goodapplications.goodapplications.Model.Artwork
import com.android.goodapplications.goodapplications.ViewModel.ArtworksViewModel
import com.android.goodapplications.goodapplications.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), ArtworkRecyclerViewAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val artworksRecyclerViewAdapter = ArtworkRecyclerViewAdapter(arrayListOf(),this)
    lateinit var viewModel: ArtworksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ArtworksViewModel::class.java)
        viewModel.loadArtworks()
        binding.viewModel = viewModel
        binding.executePendingBindings()
        binding.artworksRv.layoutManager = LinearLayoutManager(this)
        binding.artworksRv.adapter = artworksRecyclerViewAdapter
        viewModel.artworks.observe(this,
                  Observer<ArrayList<Artwork>> { it?.let{
                      artworksRecyclerViewAdapter.replaceData(it)}
                  })
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        // Associate searchable configuration with the SearchView
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(this, ArtistsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_search -> {
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onItemClick(position: Int) {
        val intent = Intent(this, SingleArtworkActivity::class.java)
        intent.putExtra("artworkInd",position)
        val selectedArtwork = artworksRecyclerViewAdapter.items[position]
        viewModel.selectedArtwork = selectedArtwork
        intent.putExtra("title",selectedArtwork.title)
        intent.putExtra("artistName",selectedArtwork.artistName)
        intent.putExtra("bodyText",selectedArtwork.bodyText)
        startActivity(intent)
    }
}
