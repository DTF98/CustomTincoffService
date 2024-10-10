package com.DTF98.TinkoffService.exeption;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@Value
public class ErrorResponse {
    List<String> errors;

    String message;

    String reason;

    String status;

    LocalDateTime timestamp;
}
