package sokeriaaa.return0.applib.locale

import java.util.Locale

actual fun LocaleHelper.getSystemLanguage(): String = Locale.getDefault().toLanguageTag()