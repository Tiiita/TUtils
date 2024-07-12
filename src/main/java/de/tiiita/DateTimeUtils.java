package de.tiiita;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtils {

    public static String formatDurationBetweenDates(OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Period period = Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
        Duration duration = Duration.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());

        if (duration.isNegative()) {
            duration = duration.plusDays(1);
            period = period.minusDays(1);
        }

        long years = period.getYears();
        long months = period.getMonths();
        long days = period.getDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        StringBuilder formattedDuration = new StringBuilder();
        if (years != 0) {
            formattedDuration.append(years).append("y ");
        }
        if (months != 0) {
            formattedDuration.append(months).append("mo ");
        }
        if (days != 0) {
            formattedDuration.append(days).append("d ");
        }
        if (hours != 0) {
            formattedDuration.append(hours).append("h ");
        }
        if (minutes != 0) {
            formattedDuration.append(minutes).append("m");
        }

        return formattedDuration.toString().trim();
    }


    /**
     * @param pattern the pattern you want the time in. Type null if you want the default one.
     * @return a string with the time in the wanted pattern or null if the pattern was invalid.
     * @throws IllegalArgumentException if the pattern was invalid.
     */
    public static String getCurrentTimeFormatted(String pattern) {
        try {
            String defaultPattern = "dd.MM.yyyy - HH:mm";
            DateFormat dateFormat;
            if (pattern == null) {
                dateFormat = new SimpleDateFormat(defaultPattern);
            } else dateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(System.currentTimeMillis());
            return dateFormat.format(date);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Duration parseDuration(String input) {
        if (input.equals("-1")) {
            return Duration.ofDays(Long.MAX_VALUE); // Representing infinite duration
        }

        // Regular expression to match sequences of digits followed by letters
        Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(input);

        long totalDays = 0;
        long totalHours = 0;
        long totalMinutes = 0;
        long totalSeconds = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();

            switch (unit) {
                case "y":
                    totalDays += value * 365L;
                    break;
                case "mo":
                    totalDays += value * 30L;
                    break;
                case "d":
                    totalDays += value;
                    break;
                case "h":
                    totalHours += value;
                    break;
                case "min":
                    totalMinutes += value;
                    break;
                case "s":
                    totalSeconds += value;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown time unit: " + unit);
            }
        }

        if (totalDays == 0 && totalHours == 0 && totalMinutes == 0 && totalSeconds == 0) {
            throw new IllegalArgumentException("No valid duration found in: " + input);
        }

        // Combine all units into a single Duration
        Duration totalDuration = Duration.ofDays(totalDays)
                .plusHours(totalHours)
                .plusMinutes(totalMinutes)
                .plusSeconds(totalSeconds);

        return totalDuration;
    }

}
