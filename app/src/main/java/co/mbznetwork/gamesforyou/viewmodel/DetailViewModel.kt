package co.mbznetwork.gamesforyou.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.mbznetwork.gamesforyou.datasource.api.model.response.GameResponse
import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import co.mbznetwork.gamesforyou.di.IODispatcher
import co.mbznetwork.gamesforyou.eventbus.UIStatusEventBus
import co.mbznetwork.gamesforyou.model.network.NetworkResult
import co.mbznetwork.gamesforyou.model.ui.GameDisplay
import co.mbznetwork.gamesforyou.model.ui.UiMessage
import co.mbznetwork.gamesforyou.model.ui.UiStatus
import co.mbznetwork.gamesforyou.repository.GameRepository
import co.mbznetwork.gamesforyou.view.GameDetailsActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val uiStatusEventBus: UIStatusEventBus,
    private val savedStateHandle: SavedStateHandle,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private val gameResponse = MutableStateFlow<GameResponse?>(null)

    val game = gameResponse.filterNotNull().map {
        GameDisplay.fromGameResponse(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), GameDisplay())

    private val menuInitialized = MutableStateFlow(false)
    val isFavorite = menuInitialized.flatMapLatest {
        if (it) gameRepository.isFavoriteGame(
            savedStateHandle.get<Int>(GameDetailsActivity.ARG_GAME_ID) ?: 0
        )
        else emptyFlow()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        getGame()
    }

    private fun getGame() {
        viewModelScope.launch(ioDispatcher) {
            savedStateHandle.get<Int>(GameDetailsActivity.ARG_GAME_ID)?.let {
                uiStatusEventBus.setUiStatus(UiStatus.Loading)
                gameRepository.getGameDetails(it).let { result ->
                    uiStatusEventBus.setUiStatus(
                        when(result) {
                            is NetworkResult.Success -> {
                                gameResponse.value = result.data
                                UiStatus.Idle
                            }
                            is NetworkResult.Error -> {
                                UiStatus.ShowError(UiMessage.StringMessage(result.message))
                            }
                        }
                    )
                }
            }
        }
    }

    fun addToFavorite() {
        viewModelScope.launch(ioDispatcher) {
            gameResponse.value?.let {
                gameRepository.addFavoriteGame(FavoriteGame.fromGameResponse(it))
            }
        }
    }

    fun removeFromFavorite() {
        viewModelScope.launch(ioDispatcher) {
            gameResponse.value?.let {
                gameRepository.removeFavoriteGame(it.id)
            }
        }
    }

    fun setMenuInitialized() {
        viewModelScope.launch(ioDispatcher) {
            menuInitialized.value = true
        }
    }
}