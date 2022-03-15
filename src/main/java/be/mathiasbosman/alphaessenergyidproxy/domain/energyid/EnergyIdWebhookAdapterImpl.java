package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;
import be.mathiasbosman.alphaessenergyidproxy.util.StreamUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
public class EnergyIdWebhookAdapterImpl implements EnergyIdWebhookAdapter {

  private final RestTemplate restTemplate;
  private final EnergyIdProperties energyIdProperties;

  @Override
  public void postData(URI targetUrl, HttpEntity<MeterReadingsDto> readings) {
    if (energyIdProperties.isMock()) {
      log.warn("Mock is set to true, no data will be exchanged");
      return;
    }
    restTemplate.postForLocation(targetUrl, readings);
  }

  /**
   * Post readings to the webhook.
   *
   * @param readingsDto {@link MeterReadingsDto} to post
   */
  @Override
  public void postReadings(MeterReadingsDto readingsDto) {
    log.info("Posting {} total data records to EnergyID", readingsDto.data().size());
    postBatchedReadings(readingsDto);
    log.info("Readings posted");
  }

  private void postBatchedReadings(MeterReadingsDto parentDto) {
    List<List<Object>> data = parentDto.data();
    StreamUtils.createBatch(data, energyIdProperties.getMaxDataBatchSize())
        .forEach(batch -> postSingleBatchReadings(parentDto, batch));
  }

  private void postSingleBatchReadings(MeterReadingsDto parentDto, List<List<Object>> data) {
    MeterReadingsDto batchDto = createBatchReadingsDto(parentDto, data);
    HttpEntity<MeterReadingsDto> batchRequest = new HttpEntity<>(batchDto);
    try {
      log.debug("Posting {} data records", batchDto.data().size());
      postData(energyIdProperties.getSecretUrl().toURI(), batchRequest);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Exception while forming URI", e);
    }
  }

  private MeterReadingsDto createBatchReadingsDto(MeterReadingsDto parentDto,
      List<List<Object>> data) {
    return new MeterReadingsDto(
        parentDto.remoteId(), parentDto.remoteName(), parentDto.metric(), parentDto.unit(),
        parentDto.readingType(), data
    );
  }
}
