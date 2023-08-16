package co.mbznetwork.gamesforyou.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import co.mbznetwork.gamesforyou.R
import co.mbznetwork.gamesforyou.di.IODispatcher
import co.mbznetwork.gamesforyou.eventbus.UIStatusEventBus
import co.mbznetwork.gamesforyou.model.ui.GameItemDisplay
import co.mbznetwork.gamesforyou.model.ui.UiMessage
import co.mbznetwork.gamesforyou.model.ui.UiStatus
import co.mbznetwork.gamesforyou.repository.GameRepository
import co.mbznetwork.gamesforyou.util.EspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val uiStatusEventBus: UIStatusEventBus,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private val search = MutableStateFlow("")
    private var searchJob: Job? = null

    val games = search.flatMapLatest {
        gameRepository.getGames(it).map { gamePaging ->
            gamePaging.map { game ->
                GameItemDisplay.fromGame(game)
            }.also {
                EspressoIdlingResource.decrement()
                uiStatusEventBus.setUiStatus(UiStatus.Idle)
            }
        }
    }.catch {
        Timber.e(it, "Error when processing Game Paging")
        uiStatusEventBus.setUiStatus(UiStatus.ShowError(it.message?.let { message ->
            UiMessage.StringMessage(message)
        } ?: UiMessage.ResourceMessage(R.string.error_occurred)))
    }.cachedIn(viewModelScope).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        PagingData.empty()
    )

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            EspressoIdlingResource.increment()
            uiStatusEventBus.setUiStatus(UiStatus.Loading)
        }
    }

    fun search(q: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(ioDispatcher) {
            EspressoIdlingResource.increment()
            _isSearching.value = q.isNotEmpty()

            q.trim().run {
                if (isNotEmpty() && length >= 3) {
                    delay(TimeUnit.SECONDS.toMillis(2))
                    search.value = this
                } else {
                    search.value = ""
                }
                EspressoIdlingResource.decrement()
            }
        }
    }
}