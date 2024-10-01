package ru.skillbox.currency.exchange.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleCurrencyDto {
    private String name;
    private Double value;
}
