package sokeriaaa.return0.applib.locale

import kotlinx.browser.window

actual fun LocaleHelper.getSystemLanguage(): String = window.navigator.language