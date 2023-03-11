package com.volie.newsapphilt.database

import androidx.room.TypeConverter
import com.volie.newsapphilt.model.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}