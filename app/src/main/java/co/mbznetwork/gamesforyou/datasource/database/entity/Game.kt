package co.mbznetwork.gamesforyou.datasource.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.mbznetwork.gamesforyou.datasource.api.model.response.GameResponse
import java.util.*

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var remoteId: Int = 0,
    var slug : String = "",
    var name : String = "",
    var released : Long = System.currentTimeMillis(),
    var tba : Boolean = false,
    @ColumnInfo("background_image")
    var backgroundImage : String = "",
    var rating : Double = .0,
    @ColumnInfo("rating_top")
    var ratingTop : Double = .0,
    @ColumnInfo("ratings_count")
    var ratingsCount : Int = 0,
    var added : Int = 0,
    var metacritic : Int = 0,
    var playtime : Int = 0,
    @ColumnInfo("suggestions_count")
    var suggestionsCount : Int = 0,
    var updated : Long = System.currentTimeMillis()
) {
    companion object {
        fun fromGameResponse(it: GameResponse) = it.run {
            Game(
                0, id, slug, name, released.time, tba, backgroundImage, rating, ratingTop,
                ratingsCount, added, metacritic, playtime, suggestionsCount, updated.time
            )
        }
    }
}
