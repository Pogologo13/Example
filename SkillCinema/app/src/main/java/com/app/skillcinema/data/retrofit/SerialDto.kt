package com.app.skillcinema.data.retrofit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.skillcinema.instance.Serial
import java.io.Serializable

@Entity(tableName = "serialList")
data class SerialDto(
    @ColumnInfo(name = "kinopoiskId")
    override var kinopoiskId: Int?,
    @ColumnInfo(name = "items")
    override val items: List<SerialItem>,
    @ColumnInfo(name = "total")
    override val total: Int,
    @PrimaryKey(autoGenerate = true)
    override val id: Int?
) : Serial

data class SerialItem(
    val episodes: List<Episode>?,
    val number: Int?
): Serializable

data class Episode(
    val episodeNumber: Int?,
    val nameEn: String?,
    val nameRu: String?,
    val releaseDate: String?,
    val seasonNumber: Int?,
    val synopsis: String?
)