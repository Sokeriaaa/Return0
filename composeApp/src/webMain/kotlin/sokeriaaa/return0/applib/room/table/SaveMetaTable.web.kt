package sokeriaaa.return0.applib.room.table

import sokeriaaa.return0.shared.data.models.title.Title

actual class SaveMetaTable actual constructor(
    actual var saveID: Int,
    actual var createdTime: Long,
    actual var savedTime: Long,
    actual var title: Title,
    actual var fileName: String,
    actual var lineNumber: Int,
)