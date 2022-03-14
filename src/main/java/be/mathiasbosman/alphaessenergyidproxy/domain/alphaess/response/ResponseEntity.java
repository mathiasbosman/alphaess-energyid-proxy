package be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ResponseEntity {
  private int code;
  private String info;

  abstract Object getData();
}
