package be.mathiasbosman.converterdataexport.collector.alphaess.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract response entity of the AlphaESS API.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class ResponseEntity {

  private int code;
  private String info;

  @SuppressWarnings("unused") // forces a data field
  abstract Object getData();
}
