package co.mbznetwork.gamesforyou.testutil

import co.mbznetwork.gamesforyou.datasource.api.model.response.GameResponse
import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import co.mbznetwork.gamesforyou.datasource.database.entity.Game
import java.util.*

object DummyGame {
    fun getDummyGames() = mutableListOf<Game>().apply {
        for (i in 1..100) {
            add(Game(
                i, i, "game$i", "Game $i", System.currentTimeMillis(), true,
                "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg",
                if (i % 2 == 0) 4.0 else 4.5, 5.0, 100+i, 10+i, 0,
                20+i, 15+i, System.currentTimeMillis()
            ))
        }
    }

    fun getDummyFavoriteGames() = mutableListOf<FavoriteGame>().apply {
        for (i in 1..10) {
            add(
                FavoriteGame(
                i, "game$i", "Game $i", System.currentTimeMillis(), true,
                "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg",
                if (i % 2 == 0) 4.0 else 4.5, 5.0, 100+i, 10+i, 0,
                20+i, 15+i, System.currentTimeMillis()
            )
            )
        }
    }

    fun getDummyGameResponse() = GameResponse(
        1, "game-1", "Game 1", Date(), true,
        "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg",
        4.5, 5.0, 100, 1000, 0, 2000, 55,
        Date()
    )
}