package co.mbznetwork.gamesforyou.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.mbznetwork.gamesforyou.config.DEFAULT_PAGE_SIZE
import co.mbznetwork.gamesforyou.datasource.api.RAWGApi
import co.mbznetwork.gamesforyou.datasource.database.dao.FavoriteGameDao
import co.mbznetwork.gamesforyou.datasource.database.dao.GameDao
import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import co.mbznetwork.gamesforyou.datasource.database.entity.Game
import co.mbznetwork.gamesforyou.repository.mediator.GameRemoteMediator
import co.mbznetwork.gamesforyou.util.ApiUtil
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val rawgApi: RAWGApi,
    private val gameRemoteMediator: GameRemoteMediator,
    private val gameDao: GameDao,
    private val favoriteGameDao: FavoriteGameDao,
    private val gson: Gson
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getGames(search: String = ""): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                initialLoadSize = 10
            ),
            remoteMediator = gameRemoteMediator.apply {
                this.search = search
            },
            pagingSourceFactory = {
                gameDao.getGames()
            }
        ).flow
    }

    fun getFavoriteGames() = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            favoriteGameDao.getFavoriteGames()
        }
    ).flow

    suspend fun getGameDetails(id: Int) = ApiUtil.finalize(gson) {
        rawgApi.getGame(id)
    }

    suspend fun addFavoriteGame(game: FavoriteGame) {
        favoriteGameDao.insert(game)
    }

    suspend fun removeFavoriteGame(id: Int) {
        favoriteGameDao.delete(id)
    }

    fun isFavoriteGame(id: Int) = favoriteGameDao.isFavoriteGame(id)
}