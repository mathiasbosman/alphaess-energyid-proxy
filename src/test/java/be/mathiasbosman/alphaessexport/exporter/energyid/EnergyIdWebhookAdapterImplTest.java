package be.mathiasbosman.alphaessexport.exporter.energyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import be.mathiasbosman.alphaessexport.exporter.energyid.dto.MeterReadingsDto;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class EnergyIdWebhookAdapterImplTest {

  private final EnergyIdProperties energyIdProperties = new EnergyIdProperties();
  @Mock
  private RestTemplate restTemplate;
  private EnergyIdWebhookAdapterImpl webhookAdapter;

  @Captor
  private ArgumentCaptor<URI> uriArgumentCaptor;
  @Captor
  private ArgumentCaptor<HttpEntity<MeterReadingsDto>> httpEntityArgumentCaptor;

  @BeforeEach
  void initAdapter() throws MalformedURLException {
    energyIdProperties.setSecretUrl(new URL("https://foo/bar"));
    webhookAdapter = new EnergyIdWebhookAdapterImpl(restTemplate, energyIdProperties);
  }

  @Test
  void postSingleBatchReadings() throws URISyntaxException {
    MeterReadingsDto readingsDto = createMeterReadingsDto(10);
    energyIdProperties.setMaxDataBatchSize(0);

    webhookAdapter.postReadings(readingsDto);

    verify(restTemplate, times(1)).postForLocation(uriArgumentCaptor.capture(),
        httpEntityArgumentCaptor.capture());

    assertThat(uriArgumentCaptor.getValue()).isEqualTo(energyIdProperties.getSecretUrl().toURI());
    assertThat(httpEntityArgumentCaptor.getValue().getBody()).isEqualTo(readingsDto);
  }

  @Test
  void postMultipleBatches() {
    MeterReadingsDto readingsDto = createMeterReadingsDto(24);
    energyIdProperties.setMaxDataBatchSize(5);

    webhookAdapter.postReadings(readingsDto);

    verify(restTemplate, times(5)).postForLocation(any(), any());
  }

  @Test
  void postReadingsDoesNotRunWhenMockIsTrue() {
    energyIdProperties.setMock(true);

    webhookAdapter.postReadings(createMeterReadingsDto(1));

    verify(restTemplate, never()).postForLocation(any(), any());
  }

  private MeterReadingsDto createMeterReadingsDto(int amountOfDataRecords) {
    List<List<Object>> data = new ArrayList<>();

    IntStream intStream = IntStream.rangeClosed(1, amountOfDataRecords);
    intStream.forEach((a) -> data.add(List.of("foo", "bar")));
    return new MeterReadingsDto(
        "remoteId", "remoteName", "metric", "unit", "readingType",
        data
    );
  }
}