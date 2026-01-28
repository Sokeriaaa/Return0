package sokeriaaa.return0.applib.datastore

import android.content.Context

actual class AppDataStoreFactory actual constructor(private val context: Any?) {

    actual fun createDataStore(fileName: String) = globalCreateDataStore(
        producePath = {
            (context as Context).filesDir.resolve("${fileName}.preferences_pb").absolutePath
        }
    )

}