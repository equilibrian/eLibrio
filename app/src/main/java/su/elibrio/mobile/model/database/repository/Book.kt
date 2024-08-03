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
    @ColumnInfo(name = "sequence") val sequence: String?,
    @ColumnInfo(name = "src") val src: String,
    @ColumnInfo(name = "hash") val hash: String
)
