package com.example.appcent.api


import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GsonDateFormatter : JsonDeserializer<Date?> {

    private val format1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    private val format2: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
    private val format3: DateFormat = SimpleDateFormat("yyy-MM-dd", Locale.US)

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date? {
        return try {
            val j = json.asJsonPrimitive.asString
            parseDate(j)
        } catch (e: ParseException) {
            throw JsonParseException(e.message, e)
        }
    }

    @Throws(ParseException::class)
    private fun parseDate(dateString: String?): Date? {
        return if (dateString != null && dateString.trim().isNotEmpty()) {
            try {
                format1.parse(dateString)
            } catch (pe: ParseException) {
                try {
                    format2.parse(dateString)
                } catch (pe2: ParseException) {
                    format3.parse(dateString)
                }
            }
        } else {
            null
        }
    }
}