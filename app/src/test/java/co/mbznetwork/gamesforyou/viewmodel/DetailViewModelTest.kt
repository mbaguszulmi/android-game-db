package co.mbznetwork.gamesforyou.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import co.mbznetwork.gamesforyou.eventbus.UIStatusEventBus
import co.mbznetwork.gamesforyou.model.network.NetworkResult
import co.mbznetwork.gamesforyou.model.ui.GameDisplay
import co.mbznetwork.gamesforyou.repository.GameRepository
import co.mbznetwork.gamesforyou.testutil.DummyGame
import co.mbznetwork.gamesforyou.testutil.MainDispatcherRule
import co.mbznetwork.gamesforyou.view.GameDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

private const val GAME_ID = 1

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var gameRepository: GameRepository

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var vm: DetailViewModel

    private val isFavorite = MutableStateFlow(false)

    private val dummyGame = DummyGame.getDummyGameResponse()

    @Before
    fun setUp() {
        runBlocking {
            Mockito.`when`(gameRepository.getGameDetails(GAME_ID)).thenReturn(
                NetworkResult.Success(dummyGame)
            )
            Mockito.`when`(savedStateHandle.get<Int>(GameDetailsActivity.ARG_GAME_ID)).thenReturn(
                GAME_ID
            )
            Mockito.`when`(gameRepository.addFavoriteGame(FavoriteGame.fromGameResponse(dummyGame)))
                .then {
                    kotlin.run { isFavorite.value = true }
                }

            Mockito.`when`(gameRepository.removeFavoriteGame(GAME_ID))
                .then {
                    kotlin.run { isFavorite.value = false }
                }

            Mockito.`when`(gameRepository.isFavoriteGame(GAME_ID)).thenReturn(
                isFavorite
            )

            vm = DetailViewModel(
                gameRepository,
                UIStatusEventBus(),
                savedStateHandle,
                Dispatchers.Main
            )
        }
    }

    @After
    fun tearDown() {
        vm.viewModelScope.cancel()
    }

    @Test
    fun `when the game is loaded, ViewModel should display the data`() = runBlocking {
        assertEquals(GameDisplay.fromGameResponse(dummyGame), vm.game.first())
    }

    @Test
    fun `ViewModel should handle favorite game state`() = runBlocking {
        assertEquals(null, vm.isFavorite.first())

        vm.addToFavorite()
        assertEquals(null, vm.isFavorite.first())

        vm.setMenuInitialized()
        assertEquals(isFavorite.value, vm.isFavorite.first())

        vm.removeFromFavorite()
        assertEquals(isFavorite.value, vm.isFavorite.first())
    }
}