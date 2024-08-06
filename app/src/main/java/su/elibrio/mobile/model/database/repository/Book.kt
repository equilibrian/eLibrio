package su.elibrio.mobile.model.database.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["hash"], unique = true)])
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "cover_page_src") val coverPageSrc: String?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "annotation") val annotation: String?,
    @ColumnInfo(name = "sequence") val sequence: String?,
    @ColumnInfo(name = "publisher") val publisher: String?,
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "lang") val lang: String?,
    @ColumnInfo(name = "translator") val translator: String?,
    @ColumnInfo(name = "isbn") val isbn: String?,
    @ColumnInfo(name = "format") val format: String?,
    @ColumnInfo(name = "size") val size: String?,
    @ColumnInfo(name = "src") val src: String,
    @ColumnInfo(name = "hash") val hash: String,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean = false
)
