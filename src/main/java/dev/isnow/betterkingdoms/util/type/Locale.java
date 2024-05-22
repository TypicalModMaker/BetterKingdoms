package dev.isnow.betterkingdoms.util.type;

import lombok.Getter;

@Getter
public enum Locale {
    ENGLISH(java.util.Locale.ENGLISH),
    GERMAN(java.util.Locale.GERMAN),
    FRENCH(java.util.Locale.FRENCH),
    JAPANESE(java.util.Locale.JAPANESE),
    ITALIAN(java.util.Locale.ITALIAN),
    KOREAN(java.util.Locale.KOREAN),
    CHINESE(java.util.Locale.CHINESE),
    SIMPLIFIED_CHINESE(java.util.Locale.SIMPLIFIED_CHINESE),
    TRADITIONAL_CHINESE(java.util.Locale.TRADITIONAL_CHINESE),
    SPANISH(new java.util.Locale("es")),
    DUTCH(new java.util.Locale("nl")),
    DANISH(new java.util.Locale("da")),
    CZECH(new java.util.Locale("cs")),
    GREEK(new java.util.Locale("el")),
    LATIN(new java.util.Locale("la")),
    BULGARIAN(new java.util.Locale("bg")),
    AFRIKAANS(new java.util.Locale("af")),
    HINDI(new java.util.Locale("hi")),
    HEBREW(new java.util.Locale("he")),
    POLISH(new java.util.Locale("pl")),
    PORTUGUESE(new java.util.Locale("pt")),
    FINNISH(new java.util.Locale("fi")),
    SWEDISH(new java.util.Locale("sv")),
    RUSSIAN(new java.util.Locale("ru")),
    ROMANIAN(new java.util.Locale("ro")),
    VIETNAMESE(new java.util.Locale("vi")),
    THAI(new java.util.Locale("th")),
    TURKISH(new java.util.Locale("tr")),
    UKRAINIAN(new java.util.Locale("uk")),
    ARABIC(new java.util.Locale("ar")),
    WELSH(new java.util.Locale("cy")),
    NORWEGIAN_BOKMAAL(new java.util.Locale("nb")),
    NORWEGIAN_NYNORSK(new java.util.Locale("nn")),
    HUNGARIAN(new java.util.Locale("hu"));

    private final java.util.Locale javaLocale;
    
    Locale(java.util.Locale locale) {
        this.javaLocale = locale;
    }
}
