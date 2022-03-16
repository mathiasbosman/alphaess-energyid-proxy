package be.mathiasbosman.alphaessexport.domain.response;

import be.mathiasbosman.alphaessexport.config.AlphaessProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Login response entity containing the {@link LoginData}.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginResponseEntity extends ResponseEntity {

  private LoginData data;

  /**
   * Data of the authentication response.
   */
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
