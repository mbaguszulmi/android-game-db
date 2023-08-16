package co.mbznetwork.gamesforyou.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.mbznetwork.gamesforyou.datasource.database.dao.FavoriteGameDao
import co.mbznetwork.gamesforyou.datasource.database.dao.GameDao
import co.mbznetwork.gamesforyou.datasource.database.dao.GameRemoteKeyDao
import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import co.mbznetwork.gamesforyou.datasource.database.entity.Game
import co.mbznetwork.gamesforyou.datasource.database.entity.GameRemoteKey

const val GAME_DB_NAME = "game_db.db"

@Database(
    entities = [Game::class, FavoriteGame::class, GameRemoteKey::class],
    version = 1
)
abstract class GameDb: RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun favoriteGameDao(): FavoriteGameDao

    abstract fun gameRemoteKeyDao(): GameRemoteKeyDao
}
