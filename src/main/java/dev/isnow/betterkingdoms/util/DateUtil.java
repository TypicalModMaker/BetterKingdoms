package dev.isnow.betterkingdoms.util;

import lombok.experimental.UtilityClass;

import java.util.Formatter;
import java.util.Locale;

@UtilityClass
public class DateUtil {
    public static String formatElapsedTime(long elapsedMilliseconds) {
        // Break the elapsed seconds into milliseconds and seconds. // From Android
        long seconds = 0;
        long milliseconds;
        if (elapsedMilliseconds >= 1000) {
            seconds = elapsedMilliseconds / 1000;
            elapsedMilliseconds -= seconds * 1000;
        }
        milliseconds = elapsedMilliseconds;
        StringBuilder sb = new StringBuilder(8);
        ;
        Formatter f = new Formatter(sb, Locale.getDefault());

        return f.format("%d.%03d", seconds, milliseconds).toString();
    }
}
