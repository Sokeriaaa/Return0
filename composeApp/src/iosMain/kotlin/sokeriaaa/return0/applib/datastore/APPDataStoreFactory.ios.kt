package sokeriaaa.return0.applib.datastore

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class AppDataStoreFactory actual constructor(context: Any?) {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(fileName: String) = globalCreateDataStore(
        producePath = {
            val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            documentDirectory?.path + "/${fileName}.preferences_pb"
        }
    )

}