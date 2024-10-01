package ru.skillbox.currency.exchange.model.jaxb;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Valute {

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "NumCode")
    private String numCode;

    @XmlElement(name = "Nominal")
    private int nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    private String value;  // Лучше обработать это как BigDecimal
}
