package sokeriaaa.return0.applib.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.nio.file.Paths

actual class AppDataStoreFactory actual constructor(context: Any?) {
    actual fun createDataStore(fileName: String): DataStore<Preferences> = globalCreateDataStore(
        producePath = {
            Paths.get("${fileName}.preferences_pb").toAbsolutePath().toString()
        },
    )
}