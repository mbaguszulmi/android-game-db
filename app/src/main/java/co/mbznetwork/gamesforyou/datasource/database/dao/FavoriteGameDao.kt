package co.mbznetwork.gamesforyou.datasource.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import co.mbznetwork.gamesforyou.datasource.database.entity.FavoriteGame
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGameDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(favoriteGame: FavoriteGame)

    @Query("DELETE FROM FavoriteGame WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM FavoriteGame")
    fun getFavoriteGames(): PagingSource<Int, FavoriteGame>

    @Query("SELECT COUNT(id) FROM FavoriteGame WHERE id = :id")
    fun isFavoriteGame(id: Int): Flow<Boolean>
}