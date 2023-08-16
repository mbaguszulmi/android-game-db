package co.mbznetwork.gamesforyou.datasource.api

import co.mbznetwork.gamesforyou.datasource.api.model.response.GameListResponse
import co.mbznetwork.gamesforyou.datasource.api.model.response.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RAWGApi {

    @GET("games")
    suspend fun getGames(
        @Query("page_size") pageSize: Int,
        @Query("page") page: Int,
        @Query("search") search: String = ""
    ): Response<GameListResponse>

    @GET("games/{id}")
    suspend fun getGame(
        @Path("id") id: Int
    ): Response<GameResponse>

}
