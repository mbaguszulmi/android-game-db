package co.mbznetwork.gamesforyou.datasource.api.typeadapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONObject
import java.io.StringReader

class GsonSanitizedTypeAdapterFactory(
    val isSanitizeRequired: Boolean = true
): TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)

        val isNotKotlinClass = type.rawType.declaredAnnotations.none {
            it.annotationClass.qualifiedName == "kotlin.Metadata"
        }
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                if (value is String && value.isEmpty()) {
                    out.nullValue()
                } else {
                    if (isNotKotlinClass) delegate.write(out, value)
                    else {
                        (value as Any?)?.javaClass?.let {
                            if (!it.name.contains("kotlin.collections") && value == it.newInstance()) out.nullValue()
                            else null
                        }?: delegate.write(out, value)
                    }
                }
            }

            override fun read(input: JsonReader?): T {
                return if (isNotKotlinClass) delegate.read(input)
                else {
                    delegate.read(
                        if (isSanitizeRequired) {
                            val mapDelegate = gson.getDelegateAdapter(
                                this@GsonSanitizedTypeAdapterFactory,
                                object : TypeToken<HashMap<*, *>>() {}
                            )
                            val data = mapDelegate.read(input).cleanUpNulls()
                            JsonReader(StringReader(JSONObject(data).toString()))
                        } else {
                            input
                        }
                    )
                }
            }
        }
    }

}

fun Map<*, *>.cleanUpNulls(): Map<*, *> =
    this.mapNotNull {
        if (it.value == null) {
            null
        } else {
            when (it.value) {
                is Map<*, *> -> {
                    it.key to (it.value as Map<*, *>).cleanUpNulls()
                }
                is List<*> -> {
                    it.key to cleanUpNullsInList(it.value as List<*>)
                }
                else -> it.key to it.value
            }
        }
    }.toMap()

private fun Map<*, *>.cleanUpNullsInList(list: List<*>): List<*> {
    return list.mapNotNull { item ->
        when (item) {
            is Map<*, *> -> {
                item.cleanUpNulls()
            }
            is List<*> -> {
                cleanUpNullsInList(item)
            }
            else -> {
                item
            }
        }
    }
}
