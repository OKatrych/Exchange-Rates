package eu.okatrych.data.serializer

import kotlinx.serialization.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import java.time.LocalDate
import java.time.LocalDateTime

@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("java.time.LocalDate", PrimitiveKind.STRING)


    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }
}

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("java.time.LocalDateTime", PrimitiveKind.STRING)


    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }
}