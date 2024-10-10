package com.DTF98.TinkoffService.model;

import lombok.Getter;

@Getter
public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    CHF("CHF"),
    JPY("JPY"),
    CNY("CNY"),
    TRY("TRY");

    private final String currency;

    Currency(String currency) {
        this.currency = currency;
    }
}
