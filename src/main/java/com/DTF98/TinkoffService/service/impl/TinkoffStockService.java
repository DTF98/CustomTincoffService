package com.DTF98.TinkoffService.service.impl;

import com.DTF98.TinkoffService.dto.*;
import com.DTF98.TinkoffService.exeption.StockNotFoundException;
import com.DTF98.TinkoffService.model.Currency;
import com.DTF98.TinkoffService.model.Stock;
import com.DTF98.TinkoffService.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
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

    @Override
    public StocksPricesDto getPrices(FigiesDto figiesDTO) {
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figiesDTO.getFigies().forEach(fg -> orderBooks.add(getOrderBookByFigi(fg)));
        var listOfOrderBooks = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock Not Found")))
                .map(orderbook -> new StockPrice(
                        orderbook.getFigi(),
                        orderbook.getLastPrice().doubleValue()
                ))
                .collect(Collectors.toList());
        return new StocksPricesDto(listOfOrderBooks);
    }

    @Async
    private CompletableFuture<MarketInstrumentList> getMarketInstrumentTickers(String ticker) {
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    @Async
    private CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        var orderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("");
        return orderBook;
    }
}
