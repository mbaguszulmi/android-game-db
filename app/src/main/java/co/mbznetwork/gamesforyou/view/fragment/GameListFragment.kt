package co.mbznetwork.gamesforyou.view.fragment

import android.content.Intent
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.mbznetwork.gamesforyou.R
import co.mbznetwork.gamesforyou.databinding.FragmentGameListBinding
import co.mbznetwork.gamesforyou.util.observeOnLifecycle
import co.mbznetwork.gamesforyou.view.GameDetailsActivity
import co.mbznetwork.gamesforyou.view.GameDetailsActivity.Companion.ARG_GAME_ID
import co.mbznetwork.gamesforyou.view.adapter.GameAdapter
import co.mbznetwork.gamesforyou.viewmodel.GameListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListFragment : BaseFragment<FragmentGameListBinding>() {
    private val gameListViewModel by viewModels<GameListViewModel>()

    private val gameAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GameAdapter {
            startActivity(Intent(
                requireContext(),
                GameDetailsActivity::class.java
            ).putExtra(ARG_GAME_ID, it))
        }
    }

    override val layoutId: Int = R.layout.fragment_game_list

    override fun initView() {
        requireBinding {
            lifecycleOwner = viewLifecycleOwner
            vm = gameListViewModel

            rvGames.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = gameAdapter.apply {
                    addLoadStateListener {
                        tvEmptyList.isVisible = if (it.append.endOfPaginationReached) {
                            gameAdapter.itemCount < 1
                        } else false
                    }
                }
            }

            srlMain.apply {
                setOnRefreshListener {
                    isRefreshing = false
                    gameAdapter.refresh()
                }
            }

            etSearch.addTextChangedListener {
                it?.toString()?.let { q -> gameListViewModel.search(q) }
            }

            ivClear.setOnClickListener {
                gameListViewModel.search("")
                etSearch.setText("")
            }
        }

        observeGames()
    }

    private fun observeGames() {
        observeOnLifecycle {
            gameListViewModel.games.collect {
                gameAdapter.submitData(it)
            }
        }
    }


}