package co.mbznetwork.gamesforyou.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import co.mbznetwork.gamesforyou.model.ui.GameItemDisplay
import co.mbznetwork.gamesforyou.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteGameViewModel @Inject constructor(
    gameRepository: GameRepository
): ViewModel() {
    val games = gameRepository.getFavoriteGames().map {
        it.map { game ->
            GameItemDisplay.fromFavoriteGame(game)
        }
    }.cachedIn(viewModelScope).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        PagingData.empty()
    )
}