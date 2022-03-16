package be.mathiasbosman.inverterdataexport.collector.alphaess.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response entity that holds {@link Statistics} data.
 */
@Getter
@Setter
@NoArgsConstructor
public class SticsByPeriodResponseEntity extends ResponseEntity {

  private Statistics data;

  /**
   * Statistics data.
   */
  @Getter
  @Setter
  @ToString
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Statistics {

    /**
     * Total PV input.
     */
    @JsonProperty("EpvT")
    private double pvTotal;
    /**
     * Total feed-in to grid.
     */
    @JsonProperty("Eout")
    private double feedIn;
    /**
     * PV to load (directly).
     */
    @JsonProperty("Epv2load")
    private double pvToLoad;
    /**
     * PV to battery.
     */
    @JsonProperty("Epvcharge")
    private double pvInput;
    /**
     * Total load.
     */
    @JsonProperty("Eload")
    private double loadTotal;
    /**
     * Grid to battery.
     */
    @JsonProperty("EGridCharge")
    private double gridInput;
    /**
     * Grid to load (directly).
     */
    @JsonProperty("EGrid2Load")
    private double gridToLoad;
  }
}
