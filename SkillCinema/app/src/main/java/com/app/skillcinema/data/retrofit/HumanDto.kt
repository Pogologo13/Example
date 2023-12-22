package com.app.skillcinema.data.retrofit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.skillcinema.instance.Human
import com.app.skillcinema.instance.HumanDetail
import java.io.Serializable

@Entity(tableName = "human")
data class HumanItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int,
    @ColumnInfo(name = "idHumanItem")
    override var kinopoiskId: Int?,
    @ColumnInfo(name = "staffId")
    override val staffId: Int?,
    @ColumnInfo(name = "description")
    override val description: String?,
    @ColumnInfo(name = "nameEn")
    override val nameEn: String?,
    @ColumnInfo(name = "nameRu")
    override val nameRu: String?,
    @ColumnInfo(name = "posterUrl")
    override val posterUrl: String?,
    @ColumnInfo(name = "professionKey")
    override val professionKey: String?,
    @ColumnInfo(name = "professionText")
    override val professionText: String?
) : Human, Serializable

@Entity(tableName = "human_detail")
data class HumanDetailDto(
    @ColumnInfo(name = "age")
    override val age: Int?,
    @ColumnInfo(name = "birthday")
    override val birthday: String?,
    @ColumnInfo(name = "birthplace")
    override val birthplace: String?,
    @ColumnInfo(name = "death")
    override val death: String?,
    @ColumnInfo(name = "deathplace")
    override val deathplace: String?,
    @ColumnInfo(name = "facts")
    override val facts: List<String>?,
    @ColumnInfo(name = "films")
    override val films: List<FilmWithActor>?,
    @ColumnInfo(name = "growth")
    override val growth: Int?,
    @ColumnInfo(name = "hasAwards")
    override val hasAwards: Int?,
    @ColumnInfo(name = "nameEn")
    override val nameEn: String?,
    @ColumnInfo(name = "nameRu")
    override val nameRu: String?,
    @PrimaryKey
    @ColumnInfo(name = "personId")
    override val personId: Int,
    @ColumnInfo(name = "posterUrl")
    override val posterUrl: String?,
    @ColumnInfo(name = "profession")
    override val profession: String?,
    @ColumnInfo(name = "sex")
    override val sex: String?,
    @ColumnInfo(name = "spouses?")
    override val spouses: List<Spouse>?,
    @ColumnInfo(name = "webUrl")
    override val webUrl: String?,
    @ColumnInfo(name = "interested")
    val interested: Boolean = false

) : HumanDetail

data class FilmWithActor(
    val description: String?,
    val filmId: Int,
    val general: Boolean,
    val nameEn: String,
    val nameRu: String,
    val professionKey: String?,
    val rating: String
)

data class Spouse(
    val children: Int,
    val divorced: Boolean,
    val divorcedReason: String,
    val name: String,
    val personId: Int,
    val relation: String,
    val sex: String,
    val webUrl: String
)

data class HumanByKeywordDto(
    val items: List<HumanItem>,
    val total: Int
)


