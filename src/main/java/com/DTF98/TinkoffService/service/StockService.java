package com.DTF98.TinkoffService.service;

import com.DTF98.TinkoffService.dto.StocksDTO;
import com.DTF98.TinkoffService.dto.TickersDTO;
import com.DTF98.TinkoffService.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDTO getStocksByTickers(TickersDTO tickersDTO);
}
