package be.mathiasbosman.converterdataexport.exporter.energyid;

import static be.mathiasbosman.converterdataexport.exporter.energyid.EnergyIdProperties.PREFIX;

import java.net.URL;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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

  /**
   * List of {@link EnergyIdMeter} meters.
   */
  @NotNull
  private List<EnergyIdMeter> meters;

  private boolean mock = false;

  /**
   * Max size of the data array to pass.
   */
  @NotNull
  private int maxDataBatchSize;

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
    private String converterId;
  }

}
