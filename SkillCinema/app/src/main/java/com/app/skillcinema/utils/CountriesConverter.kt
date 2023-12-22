package com.app.skillcinema.utils

import androidx.room.TypeConverter
import com.app.skillcinema.data.retrofit.*
import com.squareup.moshi.Moshi

object CountriesConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<Country> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Country::class.java,
        )
        val adapter = moshi.adapter<List<Country>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<Country>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Country::class.java
            )
        val adapter = moshi.adapter<List<Country>>(type)
        return adapter.toJson(value)
    }
}

object GenreConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<Genre> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Genre::class.java
        )
        val adapter = moshi.adapter<List<Genre>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<Genre>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Genre::class.java
        )
        val adapter = moshi.adapter<List<Genre>>(type)
        return adapter.toJson(value)
    }
}

object CollectionsName {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): Set<String>? {
        val type = com.squareup.moshi.Types.newParameterizedType(
            Set::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<Set<String>>(type)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromInfoType(value: Set<String>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            Set::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<Set<String>>(type)
        return adapter.toJson(value)

    }
}

object FilmConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): FilmItem {
        val type = com.squareup.moshi.Types.newParameterizedType(
            FilmItem::class.java
        )
        val adapter = moshi.adapter<FilmItem>(type)
        return adapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromInfoType(value: FilmItem): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            FilmItem::class.java
        )
        val adapter = moshi.adapter<FilmItem>(type)
        return adapter.toJson(value)
    }
}

object EpisodeConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<Episode> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Episode::class.java
        )
        val adapter = moshi.adapter<List<Episode>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<Episode>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Episode::class.java
        )
        val adapter = moshi.adapter<List<Episode>>(type)
        return adapter.toJson(value)
    }
}

object FilmItemConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<FilmItem> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            FilmItem::class.java
        )
        val adapter = moshi.adapter<List<FilmItem>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<FilmItem>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            FilmItem::class.java
        )
        val adapter = moshi.adapter<List<FilmItem>>(type)
        return adapter.toJson(value)
    }
}
object SerialDtoConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<SerialItem> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            SerialItem::class.java
        )
        val adapter = moshi.adapter<List<SerialItem>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<SerialItem>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            SerialItem::class.java
        )
        val adapter = moshi.adapter<List<SerialItem>>(type)
        return adapter.toJson(value)
    }
}
object HumanFIlmConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<FilmWithActor> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            FilmWithActor::class.java
        )
        val adapter = moshi.adapter<List<FilmWithActor>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<FilmWithActor>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            FilmWithActor::class.java
        )
        val adapter = moshi.adapter<List<FilmWithActor>>(type)
        return adapter.toJson(value)
    }
}

object SpouseConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<Spouse> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Spouse::class.java
        )
        val adapter = moshi.adapter<List<Spouse>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<Spouse>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            Spouse::class.java
        )
        val adapter = moshi.adapter<List<Spouse>>(type)
        return adapter.toJson(value)
    }
}
object ListConverter {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi) {
        this.moshi = moshi
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.fromJson(value)?.map { it } ?: emptyList()
    }

    @TypeConverter
    fun fromInfoType(value: List<String>): String {
        val type = com.squareup.moshi.Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.toJson(value)
    }
}

