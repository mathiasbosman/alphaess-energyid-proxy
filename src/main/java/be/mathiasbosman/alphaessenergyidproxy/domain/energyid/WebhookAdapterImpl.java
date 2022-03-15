package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter to interact with the EnergyID webhook.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookAdapterImpl implements EnergyIdWebhookAdapter {

  private final RestTemplate restTemplate;
  private final EnergyIdProperties energyIdProperties;

  /**
   * Post readings to the webhook.
   *
   * @param readingsDto {@link MeterReadingsDto} to post
   */
  @Override
  public void postReadings(MeterReadingsDto readingsDto) {
    boolean isMock = energyIdProperties.isMock();
    log.info("Posting {} data records to EnergyId (mock={})", readingsDto.data().size(), isMock);
    if (isMock) {
      return;
    }
    try {
      HttpEntity<MeterReadingsDto> request = new HttpEntity<>(readingsDto);
      URI uri = energyIdProperties.getSecretUrl().toURI();
      restTemplate.postForLocation(uri, request);
      log.info("Readings posted");
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Error forming URI", e);
    }
  }
}
