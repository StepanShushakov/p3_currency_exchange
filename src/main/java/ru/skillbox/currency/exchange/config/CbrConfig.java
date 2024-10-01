package ru.skillbox.currency.exchange.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cbr")
@Getter
@Setter
public class CbrConfig {
    private String url;
}
