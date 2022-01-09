package com.halrik.eidsprinten.config;

import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmm");
    }

    @Bean
    public DateTimeFormatter hourMinuteFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

}
