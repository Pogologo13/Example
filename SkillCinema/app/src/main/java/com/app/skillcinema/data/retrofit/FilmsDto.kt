package com.app.skillcinema.data.retrofit

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.skillcinema.instance.Film
import com.app.skillcinema.instance.Films
import java.io.Serializable

data class FilmsDto(
    override val items: List<FilmItem>,
    override val total: Int,
    override val totalPages: Int
) : Films

@Keep
@Entity(tableName = "film")
data class FilmItem(

    @PrimaryKey
    @ColumnInfo(name = "kinopoiskId")
    override val kinopoiskId: Int,
    val filmId: Int?,
    @ColumnInfo(name = "collectionName")
    var collectionName: Set<String>?,
    @ColumnInfo(name = "countries")
    override val countries: List<Country>?,
    @ColumnInfo(name = "genres")
    override val genres: List<Genre>?,
    @ColumnInfo(name = "imdbId")
    val imdbId: String?,
    @ColumnInfo(name = "nameEn")
    override val nameEn: String?,
    @ColumnInfo(name = "nameOriginal")
    val nameOriginal: String?,
    @ColumnInfo(name = "nameRu")
    override val nameRu: String?,
    @ColumnInfo(name = "posterUrl")
    override val posterUrl: String?,
    @ColumnInfo(name = "posterUrlPreview")
    override val posterUrlPreview: String?,
    @ColumnInfo(name = "ratingKinopoisk")
    val ratingKinopoisk: Double?,
    @ColumnInfo(name = "type")
    override val type: String?,
    @ColumnInfo(name = "year")
    override val year: Int?,
    @ColumnInfo(name = "startYear")
    val startYear: Int?,
    @ColumnInfo(name = "endYear")
    val endYear: Int?,
    @ColumnInfo(name = "completed")
    val completed: Boolean?,
    @ColumnInfo(name = "coverUrl")
    val coverUrl: String?,
    @ColumnInfo(name = "description")
    override val description: String?,
    @ColumnInfo(name = "filmLength")
    override val filmLength: String?,
    @ColumnInfo(name = "isTicketsAvailable")
    val isTicketsAvailable: Boolean?,
    @ColumnInfo(name = "kinopoiskHDId")
    val kinopoiskHDId: String?,
    @ColumnInfo(name = "ratingAgeLimits")
    val ratingAgeLimits: String?,
    @ColumnInfo(name = "serial")
    val serial: Boolean,
    @ColumnInfo(name = "shortDescription")
    val shortDescription: String?,
    @ColumnInfo(name = "webUrl")
    val webUrl: String?,
    @ColumnInfo(name = "premiere")
    var premiere: String?,
    @ColumnInfo(name = "premiereRu")
    var premiereRu: String?,
    @ColumnInfo(name = "viewed")
    var viewed: Boolean = false,
    @ColumnInfo(name = "interested")
    var interested: Boolean = false,
    ) : Film, Serializable

data class Country(
    val country: String
)

data class Genre(
    val genre: String
)

data class Gallery(
    val id: Int,
    val kinopoiskId: Int,
    val items: List<Photo>,
    val total: Int,
    val totalPages: Int,
    val type: String?
)

@Entity(tableName = "photo")
data class Photo(

    @ColumnInfo(name = "kinopoiskId")
    var kinopoiskId: Int,
    @PrimaryKey
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "previewUrl")
    val previewUrl: String,
    @ColumnInfo(name = "type")
    var type: String?
)

data class Similars(
    val items: List<SimilarItem>,
    val total: Int
)

data class SimilarItem(
    val filmId: Int,
    val nameEn: String,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val relationType: String
)








