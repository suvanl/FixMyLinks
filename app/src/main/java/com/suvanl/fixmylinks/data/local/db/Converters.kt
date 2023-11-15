package com.suvanl.fixmylinks.data.local.db

import androidx.room.TypeConverter

class Converters {
    /**
     * Converts a `List<String>` to a comma-separated string
     */
    @TypeConverter
    fun stringListToString(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    /**
     * Converts a comma-separated string to a `List<String>`
     */
    @TypeConverter
    fun stringToList(value: String?): List<String>? {
        return value?.split(",")
    }
}