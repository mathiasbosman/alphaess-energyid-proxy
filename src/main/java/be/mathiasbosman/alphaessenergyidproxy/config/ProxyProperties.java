package be.mathiasbosman.alphaessenergyidproxy.config;


import static be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.PREFIX;

import java.util.List;
import java.util.TimeZone;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
   * List of {@link EnergyIdMeter} meters.
   */
  @NotNull
  private List<EnergyIdMeter> meters;

  /**
   * Timezone used; Defaults to the default {@link TimeZone}.
   */
  private String timezone = TimeZone.getDefault().getID();
  /**
   * Cron expression for the weekly data query.
   */
  private String exportCron = "0 50 23 * * ?";

  /**
   * Energy ID meter properties. See https://api.energyid.eu/docs.html#webhook.
   */
  @Getter
  @Setter
  public static class EnergyIdMeter {

    @NotNull
    private String remoteId;
    @NotNull
    private String remoteName;
    @NotNull
    private String metric;
    @NotNull
    private String unit;
    @NotNull
    private String readingType;
    private double multiplier = 1;
    @NotNull
    private String alphaSn;
  }
}
