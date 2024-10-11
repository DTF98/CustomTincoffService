package com.DTF98.TinkoffService.service;

import com.DTF98.TinkoffService.dto.*;
import com.DTF98.TinkoffService.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDTO getStocksByTickers(TickersDTO tickersDTO);

    StocksPricesDto getPrices(FigiesDto figiesDTO);
}
