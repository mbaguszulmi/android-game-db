package co.mbznetwork.gamesforyou.model.ui

import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import co.mbznetwork.gamesforyou.datasource.database.entity.Game
import co.mbznetwork.gamesforyou.util.DateHelper

data class GameItemDisplay(
    val id: Int,
    val name: String,
    val image: String,
    val releaseDate: String,
    val rating: String
) {
    companion object {
        fun fromGame(game: Game) = game.run {
            GameItemDisplay(
                remoteId,
                name,
                backgroundImage,
                DateHelper.formatDate(released),
                String.format("%.1f", rating)
            )
        }

        fun fromFavoriteGame(game: FavoriteGame) = game.run {
            GameItemDisplay(
                id,
                name,
                backgroundImage,
                DateHelper.formatDate(released),
                String.format("%.1f", rating)
            )
        }
    }
}
