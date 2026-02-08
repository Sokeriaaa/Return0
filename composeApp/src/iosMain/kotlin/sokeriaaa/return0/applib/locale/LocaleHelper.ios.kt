package sokeriaaa.return0.applib.locale

import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

actual fun LocaleHelper.getSystemLanguage(): String =
    // "en_US" -> "en-US"
    platform.Foundation.NSLocale.currentLocale.localeIdentifier.replace("_", "-")