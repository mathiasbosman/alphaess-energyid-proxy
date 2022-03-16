package be.mathiasbosman.alphaessexport.config;


import static be.mathiasbosman.alphaessexport.config.ProxyProperties.PREFIX;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties of the main application.
 */
@Data
@Validated
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = PREFIX)
public class ProxyProperties {

  public static final String PREFIX = "proxy";

  /**
   * Cron expression for the weekly data query.
   */
  private String exportCron = "0 50 23 * * ?";

}
