package co.mbznetwork.gamesforyou.view.fragment

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.mbznetwork.gamesforyou.R
import co.mbznetwork.gamesforyou.databinding.FragmentFavoriteGameBinding
import co.mbznetwork.gamesforyou.util.observeOnLifecycle
import co.mbznetwork.gamesforyou.view.GameDetailsActivity
import co.mbznetwork.gamesforyou.view.adapter.GameAdapter
import co.mbznetwork.gamesforyou.viewmodel.FavoriteGameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteGameFragment : BaseFragment<FragmentFavoriteGameBinding>() {

    private val favoriteGameViewModel by viewModels<FavoriteGameViewModel>()

    private val gameAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GameAdapter {
            startActivity(
                Intent(
                requireContext(),
                GameDetailsActivity::class.java
            ).putExtra(GameDetailsActivity.ARG_GAME_ID, it))
        }
    }

    override val layoutId: Int = R.layout.fragment_favorite_game

    override fun initView() {
        requireBinding {
            rvFavoriteGames.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = gameAdapter.apply {
                    addLoadStateListener {
                        tvEmptyList.isVisible = if (it.append.endOfPaginationReached) {
                            gameAdapter.itemCount < 1
                        } else false
                    }
                }
            }
        }

        observeFavoriteGames()
    }

    private fun observeFavoriteGames() {
        observeOnLifecycle {
            favoriteGameViewModel.games.collect {
                gameAdapter.submitData(it)
            }
        }
    }

}