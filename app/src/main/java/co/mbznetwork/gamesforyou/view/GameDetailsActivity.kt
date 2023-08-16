package co.mbznetwork.gamesforyou.view

import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import co.mbznetwork.gamesforyou.R
import co.mbznetwork.gamesforyou.databinding.ActivityGameDetailsBinding
import co.mbznetwork.gamesforyou.util.observeOnLifecycle
import co.mbznetwork.gamesforyou.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailsActivity : BaseActivity<ActivityGameDetailsBinding>() {
    private val detailViewModel by viewModels<DetailViewModel>()

    private lateinit var menu: Menu

    override val layoutId: Int = R.layout.activity_game_details

    override fun initView() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detail)
        }

        binding.apply {
            lifecycleOwner = this@GameDetailsActivity
            vm = detailViewModel
        }

        observeFavoriteState()
    }

    private fun observeFavoriteState() {
        observeOnLifecycle {
            detailViewModel.isFavorite.collect {
                it?.let {
                    menu.apply {
                        findItem(R.id.menu_add_favorite).isVisible = !it
                        findItem(R.id.menu_remove_favorite).isVisible = it
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        detailViewModel.setMenuInitialized()
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_add_favorite -> detailViewModel.addToFavorite()
            R.id.menu_remove_favorite -> detailViewModel.removeFromFavorite()
        }
        return true
    }

    companion object {
        const val ARG_GAME_ID = "arg_game_id"
    }
}