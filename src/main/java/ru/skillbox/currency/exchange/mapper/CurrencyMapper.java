package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.model.dto.SimpleCurrencyDto;
import ru.skillbox.currency.exchange.model.entity.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    SimpleCurrencyDto convertToSimpleDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);
}
