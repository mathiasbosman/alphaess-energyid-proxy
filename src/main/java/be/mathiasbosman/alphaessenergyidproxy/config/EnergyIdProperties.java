package be.mathiasbosman.alphaessenergyidproxy.config;

import static be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties.PREFIX;

import java.net.URL;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

/**
 * EnergyID properties.
 */
@Data
@Validated
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = PREFIX)
public class EnergyIdProperties {

  public static final String PREFIX = "energyid";

  /**
   * Secret {@link URL} of the webhook.
   */
  @NotNull
  private URL secretUrl;

  private boolean mock = false;

  /**
   * Max size of the data array to pass.
   */
  @NotNull
  private int maxDataBatchSize;

}
