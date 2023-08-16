package co.mbznetwork.gamesforyou.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import co.mbznetwork.gamesforyou.config.GAME_INITIAL_PAGE
import co.mbznetwork.gamesforyou.datasource.api.RAWGApi
import co.mbznetwork.gamesforyou.datasource.database.GameDb
import co.mbznetwork.gamesforyou.datasource.database.dao.GameDao
import co.mbznetwork.gamesforyou.datasource.database.dao.GameRemoteKeyDao
import co.mbznetwork.gamesforyou.datasource.database.entity.Game
import co.mbznetwork.gamesforyou.datasource.database.entity.GameRemoteKey
import co.mbznetwork.gamesforyou.model.network.NetworkResult
import co.mbznetwork.gamesforyou.util.ApiUtil
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class GameRemoteMediator @Inject constructor(
    private val database: GameDb,
    private val gameDao: GameDao,
    private val rawgAPI: RAWGApi,
    private val gson: Gson,
    private val gameRemoteKeyDao: GameRemoteKeyDao
): RemoteMediator<Int, Game>() {

    var search: String = ""

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Game>): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                getRemoteKeyClosestToCurrentPosition(state)?.nextKey
                    ?.minus(1) ?: GAME_INITIAL_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }

        return ApiUtil.finalize(gson) {
            rawgAPI.getGames(state.config.pageSize, page, search)
        }.let {
            when(it) {
                is NetworkResult.Success -> {
                    val games = it.data.results.map { gameResponse ->
                        Game.fromGameResponse(gameResponse)
                    }
                    val endOfPaginationReached = games.isEmpty()

                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            gameRemoteKeyDao.deleteAll()
                            gameDao.deleteAll()
                        }

                        val prevKey = if (page == GAME_INITIAL_PAGE) null else page - 1
                        val nextKey = if (endOfPaginationReached) null else page + 1

                        gameRemoteKeyDao.insert(games.map { game ->
                            GameRemoteKey(game.remoteId, prevKey, nextKey)
                        })
                        gameDao.insert(games)
                    }
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                is NetworkResult.Error -> {
                    MediatorResult.Error(Throwable(it.message))
                }
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Game>) =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            gameRemoteKeyDao.getRemoteKeyById(it.remoteId)
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Game>) =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            gameRemoteKeyDao.getRemoteKeyById(it.remoteId)
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Game>) =
        with(state) {
            anchorPosition?.let { closestItemToPosition(it) }?.remoteId?.let {
                gameRemoteKeyDao.getRemoteKeyById(it)
            }
        }
}