package be.mathiasbosman.inverterdataexport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main proxy application.
 */
@SpringBootApplication
public class InverterDataExportApplication {

  public static void main(String[] args) {
    SpringApplication.run(InverterDataExportApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

}
