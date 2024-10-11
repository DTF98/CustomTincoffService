package com.DTF98.TinkoffService.controller;

import com.DTF98.TinkoffService.dto.FigiesDto;
import com.DTF98.TinkoffService.dto.StocksDTO;
import com.DTF98.TinkoffService.dto.StocksPricesDto;
import com.DTF98.TinkoffService.dto.TickersDTO;
import com.DTF98.TinkoffService.model.Stock;
import com.DTF98.TinkoffService.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDTO getStocksByTicker(@RequestBody TickersDTO tickersDTO) {
        return stockService.getStocksByTickers(tickersDTO);
    }

    @PostMapping("/prices/")
    public StocksPricesDto getPrices(@RequestBody FigiesDto figiesDto) {
        return stockService.getPrices(figiesDto);
    }
}
