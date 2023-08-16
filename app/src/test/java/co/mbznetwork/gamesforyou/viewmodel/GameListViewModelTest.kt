package co.mbznetwork.gamesforyou.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.mbznetwork.gamesforyou.eventbus.UIStatusEventBus
import co.mbznetwork.gamesforyou.model.ui.GameItemDisplay
import co.mbznetwork.gamesforyou.repository.GameRepository
import co.mbznetwork.gamesforyou.testutil.DummyGame
import co.mbznetwork.gamesforyou.testutil.MainDispatcherRule
import co.mbznetwork.gamesforyou.testutil.generateDiffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GameListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var gameRepository: GameRepository

    @Mock
    private lateinit var uiStatusEventBus: UIStatusEventBus

    private lateinit var vm: GameListViewModel

    @Before
    fun setUp() {
        vm = GameListViewModel(
            gameRepository,
            uiStatusEventBus,
            Dispatchers.Main
        )
    }

    @After
    fun tearDown() {
        vm.viewModelScope.cancel()
    }

    @Test
    fun `when game exists, ViewModel should return data`() = runBlocking {
        val dummyGames = DummyGame.getDummyGames()
        Mockito.`when`(gameRepository.getGames()).thenReturn(flow {
            emit(PagingData.from(dummyGames))
        })

        val actualGames = vm.games.first()

        val differ = generateDiffer()
        differ.submitData(actualGames)

        assertEquals(dummyGames.size, differ.snapshot().size)
        assertEquals(GameItemDisplay.fromGame(dummyGames.first()), differ.snapshot().first())
    }

    @Test
    fun `when game doesn't exist, ViewModel should return empty data`() = runBlocking {
        Mockito.`when`(gameRepository.getGames()).thenReturn(flow {
            emit(PagingData.empty())
        })

        val actualGames = vm.games.first()

        val differ = generateDiffer()
        differ.submitData(actualGames)
        assertEquals(0, differ.snapshot().size)
    }

    @Test
    fun `when search is performed, isSearching should be true`() {

        val keyword = "Grand"
        vm.search(keyword)

        assertEquals(true, vm.isSearching.value)

        vm.search("")
        assertEquals(false, vm.isSearching.value)
    }
}