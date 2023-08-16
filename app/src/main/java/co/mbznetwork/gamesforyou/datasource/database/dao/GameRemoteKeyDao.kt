package co.mbznetwork.gamesforyou.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.mbznetwork.gamesforyou.datasource.database.entity.GameRemoteKey

@Dao
interface GameRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeys: List<GameRemoteKey>)

    @Query("DELETE FROM GameRemoteKey")
    suspend fun deleteAll()

    @Query("SELECT * FROM GameRemoteKey WHERE id = :id")
    suspend fun getRemoteKeyById(id: Int): GameRemoteKey?
}