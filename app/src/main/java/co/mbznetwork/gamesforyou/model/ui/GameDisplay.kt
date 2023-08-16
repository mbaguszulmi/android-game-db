package co.mbznetwork.gamesforyou.model.ui

import co.mbznetwork.gamesforyou.datasource.api.model.response.GameResponse
import co.mbznetwork.gamesforyou.util.DateHelper

data class GameDisplay(
    val id: Int = 0,
    val name: String = "",
    val image: String = "",
    val releaseDate: String = "",
    val rating: String = "",
    val played: Int = 0,
    val description: String = "",
    val publisher: String = ""
) {
    companion object {
        fun fromGameResponse(game: GameResponse) = game.run {
            GameDisplay(
                id,
                name,
                backgroundImage,
                DateHelper.formatDate(released),
                String.format("%.1f", rating),
                added,
                description,
                publishers.firstOrNull()?.name ?: ""
            )
        }
    }
}
