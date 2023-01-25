package at.htlwels.klackl.timerecording.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeStringFormatter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatString(long millis1, long millis2) {
        LocalDateTime date1 =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millis1), ZoneId.systemDefault());

        LocalDateTime date2 =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millis2), ZoneId.systemDefault());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateAndTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        if(dateFormatter.format(date1).equals(dateFormatter.format(date2))) {
            return String.format("%s: %s - %s",
                    dateFormatter.format(date1),
                    timeFormatter.format(date1),
                    timeFormatter.format(date2)
            );
        }else {
            return String.format("%s - %s",
                    dateAndTimeFormatter.format(date1),
                    dateAndTimeFormatter.format(date2)
            );
        }
    }
}
