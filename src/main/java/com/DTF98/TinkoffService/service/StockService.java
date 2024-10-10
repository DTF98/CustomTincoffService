package com.DTF98.TinkoffService.service;

import com.DTF98.TinkoffService.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
