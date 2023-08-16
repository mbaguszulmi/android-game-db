package co.mbznetwork.gamesforyou.datasource.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameRemoteKey(
    @PrimaryKey
    val id: Int,
    val prevKey: Int? = null,
    val nextKey: Int? = null
)
