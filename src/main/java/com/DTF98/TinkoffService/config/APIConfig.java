package com.DTF98.TinkoffService.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "api")
public class APIConfig {
     private boolean isSandBoxMode;
}
