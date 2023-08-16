package co.mbznetwork.gamesforyou.datasource.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import co.mbznetwork.gamesforyou.datasource.database.entity.Game

@Dao
interface GameDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(games: List<Game>)

    @Query("DELETE FROM Game")
    suspend fun deleteAll()

    @Query("SELECT * FROM Game")
    fun getGames(): PagingSource<Int, Game>
}