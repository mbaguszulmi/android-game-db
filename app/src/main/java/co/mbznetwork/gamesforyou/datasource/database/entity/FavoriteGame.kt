package co.mbznetwork.gamesforyou.datasource.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.mbznetwork.gamesforyou.datasource.api.model.response.GameResponse
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class FavoriteGame(
    @PrimaryKey
    var id : Int = 0,
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
    var updated : Long = System.currentTimeMillis(),
    val description : String = "",
    val publisherName: String = ""
): Parcelable {
    companion object {
        fun fromGameResponse(it: GameResponse) = it.run {
            FavoriteGame(
                id, slug, name, released.time, tba, backgroundImage, rating, ratingTop,
                ratingsCount, added, metacritic, playtime, suggestionsCount, updated.time,
                description,
                publishers.firstOrNull()?.name ?: ""
            )
        }
    }
}
