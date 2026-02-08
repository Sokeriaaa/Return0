package sokeriaaa.return0.applib.locale

import android.os.Build
import java.util.Locale

actual fun LocaleHelper.getSystemLanguage(): String {
    val locale = Locale.getDefault()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        locale.toLanguageTag()
    } else {
        // Fallback for older environments: reconstruct "en-US" manually
        val language = locale.language
        val country = locale.country
        if (country.isNotEmpty()) "$language-$country" else language
    }
}