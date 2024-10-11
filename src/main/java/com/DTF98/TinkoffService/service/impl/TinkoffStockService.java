package com.DTF98.TinkoffService.service.impl;

import com.DTF98.TinkoffService.dto.TickersDTO;
import com.DTF98.TinkoffService.exeption.StockNotFoundException;
import com.DTF98.TinkoffService.model.Currency;
import com.DTF98.TinkoffService.model.Stock;
import com.DTF98.TinkoffService.dto.StocksDTO;
import com.DTF98.TinkoffService.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi openApi;

    @Override
    public Stock getStockByTicker(String ticker) {
        var cf = getMarketInstrumentTickers(ticker);
        var list = cf.join().getInstruments();
        if (list.isEmpty()) {
             throw new StockNotFoundException(String.format("Stock %S not found.", ticker));
        }
        var item = list.get(0);
        return new Stock(item.getTicker(), item.getFigi(), item.getName(), item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()), "TINKOFF");
    }

    public StocksDTO getStocksByTickers(TickersDTO tickersDTO) {
        List<CompletableFuture<MarketInstrumentList>> marketInstrument = new ArrayList<>();
        tickersDTO.getTickers().forEach(ticker -> marketInstrument.add(getMarketInstrumentTickers(ticker)));
        List<Stock> stocks = marketInstrument.stream().
                map(CompletableFuture::join)
                .map(mi -> {
                    if (!mi.getInstruments().isEmpty()) {
                        return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                ))
                .collect(Collectors.toList());
        return new StocksDTO(stocks);
    }

    @Async
    private CompletableFuture<MarketInstrumentList> getMarketInstrumentTickers(String ticker) {
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }
}
