package be.mathiasbosman.inverterdataexport.collector.alphaess.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response entity with {@link EssSystemData}.
 */
@Getter
@Setter
@NoArgsConstructor
public class EssListResponseEntity extends ResponseEntity {

  private List<EssSystemData> data;

  /**
   * Data of a given EssSystem.
   */
  @Getter
  @Setter
  @NoArgsConstructor
  @ToString(of = {"sysSn", "sysName", "emsStatus"})
  public static class EssSystemData {

    @JsonProperty("sys_sn")
    private String sysSn;
    @JsonProperty("sys_name")
    private String sysName;
    private double popv;
    private String minv;
    private double poinv;
    private double cobat;
    private String mbat;
    private double surpluscobat;
    private double uscapacity;
    @JsonProperty("ems_status")
    private String emsStatus;
    @JsonProperty("trans_frequency")
    private int transFrequency;
  }

}
