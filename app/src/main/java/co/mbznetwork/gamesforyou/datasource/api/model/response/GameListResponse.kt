package co.mbznetwork.gamesforyou.datasource.api.model.response

data class GameListResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<GameResponse> = emptyList()
)
