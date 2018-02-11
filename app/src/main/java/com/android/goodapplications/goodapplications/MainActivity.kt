package com.android.goodapplications.goodapplications




import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.android.goodapplications.goodapplications.ViewModel.ArtistsViewModel
import com.android.goodapplications.goodapplications.ViewModel.ArtworksViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.widget.SearchView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var artworkViewModel : ArtworksViewModel
    private lateinit var artistViewModel : ArtistsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        artworkViewModel = ViewModelProviders.of(this).get(ArtworksViewModel::class.java)
        artistViewModel = ViewModelProviders.of(this).get(ArtistsViewModel::class.java)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        loadData()
        handleIntent(intent)
        replaceFragment(ArtworksFragment.newInstance(),R.id.root_layout,"ArtworksFragment")
    }

    private fun handleIntent(intent : Intent?)
    {
        if (intent != null) {
            if (Intent.ACTION_SEARCH == intent.action)
            {
                val query = intent.getStringExtra(SearchManager.QUERY)
                sendQueryToViewModel(query)
            }
        }
    }

    fun sendQueryToViewModel(str:String)
    {
        val fragment = getVisibleFragment()
        if( null!=fragment && fragment is ArtworksFragment )
            artworkViewModel.searchArtworks(str)
        else if( null!=fragment && fragment is ArtistsFragment )
            artistViewModel.searchArtists(str)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    private fun loadData()
    {
        artworkViewModel.loadArtworks()
        artistViewModel.loadArtists()
    }

    private fun getVisibleFragment(): Fragment? {
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible)
                    return fragment
            }
        }
        return null
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
    /*
    Now to add a Fragment, we can call like this from any Activity:
        supportFragmentManager.inTransaction {
                add(R.id.frameLayoutContent, fragment)
        }
     */

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int,tag: String){
        supportFragmentManager.inTransaction { add(frameId, fragment, tag) }
    }

    private fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String) {
        supportFragmentManager.inTransaction{replace(frameId, fragment, tag) }
    }
    /*
    Using the above extension functions, now we can add or replace aFragment from any Activity in a single line, without any additional qualifiers:
    addFragment(fragment, R.id.fragment_container)
    replaceFragment(fragment, R.id.fragment_container)
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_bar).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        addOnQueryTextListener(searchView)
        return true
    }

    private fun addOnQueryTextListener(searchView: SearchView)
    {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText==null||newText=="") artworkViewModel.resetSearchRes()
                else sendQueryToViewModel(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query==null||query=="") artworkViewModel.resetSearchRes()
                else sendQueryToViewModel(query)
                searchView.clearFocus()
                return true
            }


        })
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
            R.id.nav_artists ->
            {
                artistViewModel.resetSearchRes()
                replaceFragment(ArtistsFragment.newInstance(),R.id.root_layout,"ArtistsFragment")
            }
            R.id.nev_artworks ->
            {
                artworkViewModel.resetSearchRes()
                replaceFragment(ArtworksFragment.newInstance(),R.id.root_layout,"ArtworksFragment")
            }
            R.id.nav_search ->
            {

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

}
