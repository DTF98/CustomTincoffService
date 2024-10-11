package com.DTF98.TinkoffService.dto;

import com.DTF98.TinkoffService.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StocksDTO {
    List<Stock> stocks;
}
