package co.mbznetwork.gamesforyou.datasource.api.model.response

import co.mbznetwork.gamesforyou.datasource.api.typeadapter.DateTimeTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

data class GameResponse (
    var id : Int = 0,
    var slug : String = "",
    var name : String = "",
    var released : Date = Date(),
    var tba : Boolean = false,
    @SerializedName("background_image")
    var backgroundImage : String = "",
    var rating : Double = .0,
    @SerializedName("rating_top")
    var ratingTop : Double = .0,
    @SerializedName("ratings_count")
    var ratingsCount : Int = 0,
    var added : Int = 0,
    var metacritic : Int = 0,
    var playtime : Int = 0,
    @SerializedName("suggestions_count")
    var suggestionsCount : Int = 0,
    @JsonAdapter(DateTimeTypeAdapter::class)
    var updated : Date = Date(),
    val description : String = "",
    val publishers: List<GamePublisher> = emptyList()
)

data class GamePublisher(
    val id : Int = 0,
    val name : String = "",
    val slug : String = ""
)
