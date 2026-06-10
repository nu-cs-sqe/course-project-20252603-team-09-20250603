package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class I18n {
    private static final String BUNDLE_BASE_NAME = "labels";
    private static Locale locale = Locale.US;

    private I18n() {
    }

    public static void setLocale(Locale newLocale) {
        locale = newLocale == null ? Locale.US : newLocale;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static String text(String key, Object... args) {
        String pattern;

        try {
            pattern = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale).getString(key);
        } catch (MissingResourceException e) {
            try {
                pattern = ResourceBundle.getBundle(BUNDLE_BASE_NAME, Locale.US).getString(key);
            } catch (MissingResourceException fallbackException) {
                return key;
            }
        }

        return args == null || args.length == 0 ? pattern : MessageFormat.format(pattern, args);
    }
}
