package be.mathiasbosman.converterdataexport.exporter.energyid;

import be.mathiasbosman.converterdataexport.exporter.energyid.dto.MeterReadingsDto;
import be.mathiasbosman.converterdataexport.util.CollectionUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter to interact with the EnergyID webhook. EnergyID only allows a certain amount of data
 * points per post. Make sure to configure it as needed. If set the data will be split into batches
 * if it exceeds the maximum amount. For more information check https://api.energyid.eu/docs.html#webhook.
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
    CollectionUtils.split(data, energyIdProperties.getMaxDataBatchSize())
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
