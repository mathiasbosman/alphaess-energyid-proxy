package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookAdapter {

  private final ObjectMapper mapper;
  private final RestTemplate restTemplate;
  private final EnergyIdProperties energyIdProperties;

  public void postReadings(MeterReadingsDto readingsDto) {
    try {
      log.info("Posting readings to EnergyId");
      log.debug("Body: {}", mapper.writeValueAsString(readingsDto));
      HttpEntity<MeterReadingsDto> request = new HttpEntity<>(readingsDto);
      restTemplate.postForLocation(energyIdProperties.getSecretUrl().toURI(), request);
      log.info("Readings posted");
    } catch (Exception e) {
      log.error("Exception while posting data", e);
    }
  }
}
