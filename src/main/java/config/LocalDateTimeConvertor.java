package config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConvertor {

    public LocalDateTime convert(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy | HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

}
