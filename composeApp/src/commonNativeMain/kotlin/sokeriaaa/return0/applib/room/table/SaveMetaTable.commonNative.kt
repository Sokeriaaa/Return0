package sokeriaaa.return0.applib.room.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sokeriaaa.return0.shared.data.models.title.Title

@Entity(
    tableName = SaveMetaTable.TABLE_NAME,
)
actual class SaveMetaTable actual constructor(
    @PrimaryKey
    @ColumnInfo(name = "save_id")
    actual var saveID: Int,
    @ColumnInfo(name = "created_timed")
    actual var createdTime: Long,
    @ColumnInfo(name = "saved_timed")
    actual var savedTime: Long,
    @ColumnInfo(name = "title")
    actual var title: Title,
    @ColumnInfo(name = "file_name")
    actual var fileName: String,
    @ColumnInfo(name = "line_number")
    actual var lineNumber: Int,
) {
    companion object {
        const val TABLE_NAME = "return0_save_metas"
    }
}