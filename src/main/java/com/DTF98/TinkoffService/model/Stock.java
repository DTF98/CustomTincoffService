package com.DTF98.TinkoffService.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Stock {
    String ticker;
    String figi; // модификатор цены
    String name;
    String type;
    Currency currency;
    String source; // откуда нам доставили акцию

}
