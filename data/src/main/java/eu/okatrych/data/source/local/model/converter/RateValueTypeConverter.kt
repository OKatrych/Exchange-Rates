package eu.okatrych.data.source.local.model.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import eu.okatrych.domain.model.RateValue

class RateValueTypeConverter {

    private val moshi: Moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    fun fromRateValueList(value: List<RateValue>): String {
        val type = Types.newParameterizedType(List::class.java, RateValue::class.java)
        val adapter = moshi.adapter<List<RateValue>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toRateValueList(value: String): List<RateValue> {
        val type = Types.newParameterizedType(List::class.java, RateValue::class.java)
        val adapter = moshi.adapter<List<RateValue>>(type)
        return adapter.fromJson(value)!!
    }
}