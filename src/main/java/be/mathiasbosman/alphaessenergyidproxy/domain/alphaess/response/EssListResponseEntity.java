package be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class EssListResponseEntity extends ResponseEntity {

  private List<EssSystemData> data;

  @Getter
  @Setter
  @ToString(of = {"sysSn", "sysName", "emsStatus"})
  @NoArgsConstructor
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
