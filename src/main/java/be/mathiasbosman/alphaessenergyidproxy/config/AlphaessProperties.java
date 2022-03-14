package be.mathiasbosman.alphaessenergyidproxy.config;

import static be.mathiasbosman.alphaessenergyidproxy.config.AlphaessProperties.PREFIX;

import java.net.URL;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = PREFIX)
public class AlphaessProperties {

  public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

  public static final String PREFIX = "alphaess";

  @NotNull
  private URL baseUrl;

  @NotNull
  private Credentials credentials;

  @Getter
  @Setter
  public static class Credentials {
    @NotNull
    private String username;
    @NotNull
    private String password;
  }
}
