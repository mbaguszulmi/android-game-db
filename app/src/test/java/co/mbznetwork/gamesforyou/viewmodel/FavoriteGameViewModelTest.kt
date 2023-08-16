package co.mbznetwork.gamesforyou.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import co.mbznetwork.gamesforyou.model.ui.GameItemDisplay
import co.mbznetwork.gamesforyou.repository.GameRepository
import co.mbznetwork.gamesforyou.testutil.DummyGame
import co.mbznetwork.gamesforyou.testutil.MainDispatcherRule
import co.mbznetwork.gamesforyou.testutil.generateDiffer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FavoriteGameViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var gameRepository: GameRepository

    private lateinit var vm: FavoriteGameViewModel

    @After
    fun tearDown() {
        vm.viewModelScope.cancel()
    }

    @Test
    fun `when game exists, ViewModel should return data`() = runBlocking {
        val dummyGames = DummyGame.getDummyFavoriteGames()
        Mockito.`when`(gameRepository.getFavoriteGames()).thenReturn(
            flow { emit(PagingData.from(dummyGames)) }
        )

        vm = FavoriteGameViewModel(gameRepository)

        val actualGames = vm.games.first()

        val differ = generateDiffer()
        differ.submitData(actualGames)

        assertEquals(dummyGames.size, differ.snapshot().size)
        assertEquals(GameItemDisplay.fromFavoriteGame(dummyGames.first()), differ.snapshot().first())
    }

    @Test
    fun `when game doesn't exist, ViewModel should return empty data`() = runBlocking {
        Mockito.`when`(gameRepository.getFavoriteGames()).thenReturn(
            flow { emit(PagingData.empty()) }
        )

        vm = FavoriteGameViewModel(gameRepository)

        val actualGames = vm.games.first()

        val differ = generateDiffer()
        differ.submitData(actualGames)

        assertEquals(0, differ.snapshot().size)
    }
}