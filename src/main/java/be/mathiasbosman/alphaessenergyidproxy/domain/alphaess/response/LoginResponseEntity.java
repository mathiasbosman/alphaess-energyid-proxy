package be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response;

import be.mathiasbosman.alphaessenergyidproxy.config.AlphaessProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseEntity extends ResponseEntity {

  private LoginData data;

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LoginData {

    private String accessToken;
    private double expiresIn;
    @JsonFormat(pattern = AlphaessProperties.DATE_TIME_FORMAT_PATTERN)
    private Date tokenCreateTime;
    private String refreshTokenKey;
  }
}
