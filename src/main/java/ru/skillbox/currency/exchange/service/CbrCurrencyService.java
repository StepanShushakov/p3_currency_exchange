package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.currency.exchange.config.CbrConfig;
import ru.skillbox.currency.exchange.model.entity.Currency;
import ru.skillbox.currency.exchange.model.jaxb.ValCurs;
import ru.skillbox.currency.exchange.model.jaxb.Valute;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class CbrCurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CbrConfig cbrConfig;
    private final RestTemplate restTemplate;

    @Scheduled(fixedRateString = "${cbr.checkFrequency}")
    public void updateCurrencyData() {
        try {
            // Получаем XML от ЦБ РФ
            String xml = restTemplate.getForObject(cbrConfig.getUrl(), String.class);

            // Парсим XML в объекты
            JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            assert xml != null;
            ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(new StringReader(xml));

            // Обрабатываем и обновляем данные
            valCurs.getValutes().forEach(this::saveOrUpdateCurrency);
        } catch (Exception e) {
            log.error("Error while updating currency data from CBR", e);
        }
    }

    private void saveOrUpdateCurrency(Valute valute) {
        // Ищем валюту по ISO_Char_Code
        Currency existingCurrency = currencyRepository.findByIsoCharCode(valute.getCharCode());
        if (existingCurrency == null) {
            existingCurrency = currencyRepository.findByIsoNumCode(Long.valueOf(valute.getNumCode()));
        }

        if (existingCurrency != null) {
            // Обновляем данные
            fillCurrency(existingCurrency, valute);
            currencyRepository.save(existingCurrency);
        } else {
            // Создаем новую запись
            Currency newCurrency = new Currency();
            fillCurrency(newCurrency, valute);
            currencyRepository.save(newCurrency);
        }
    }

    private void fillCurrency(Currency currency, Valute valute) {
        currency.setIsoCharCode(valute.getCharCode());
        currency.setIsoNumCode(Long.valueOf(valute.getNumCode()));
        currency.setName(valute.getName());
        currency.setNominal((long) valute.getNominal());
        currency.setValue(Double.valueOf(valute.getValue().replace(",", ".")));
    }
}
