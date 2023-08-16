package co.mbznetwork.gamesforyou.util

import co.mbznetwork.gamesforyou.datasource.api.model.response.ErrorResponse
import co.mbznetwork.gamesforyou.model.network.NetworkResult
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import timber.log.Timber

object ApiUtil {
    suspend fun <T>finalize(gson: Gson, call: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error("Error occurred")
            } else {
                response.errorBody()?.let {
                    try {
                        val body = gson.fromJson(
                            it.charStream(),
                            ErrorResponse::class.java
                        ) as ErrorResponse
                        NetworkResult.Error(body.detail)
                    } catch (e: JsonSyntaxException) {
                        NetworkResult.Error(response.message())
                    }
                } ?: NetworkResult.Error("Error occurred")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error when processing request.")
            NetworkResult.Error(e.message ?: "Error occurred")
        }
    }
}
