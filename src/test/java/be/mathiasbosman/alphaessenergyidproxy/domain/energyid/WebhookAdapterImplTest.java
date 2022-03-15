package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
class WebhookAdapterImplTest {

  private final EnergyIdProperties energyIdProperties = new EnergyIdProperties();
  @Mock
  private RestTemplate restTemplate;
  private WebhookAdapterImpl webhookAdapter;

  @Captor
  private ArgumentCaptor<URI> uriArgumentCaptor;
  @Captor
  private ArgumentCaptor<HttpEntity<MeterReadingsDto>> httpEntityArgumentCaptor;

  @BeforeEach
  void initAdapter() throws MalformedURLException {
    energyIdProperties.setSecretUrl(new URL("https://foo/bar"));
    webhookAdapter = new WebhookAdapterImpl(restTemplate, energyIdProperties);
  }

  @Test
  void postReadings() throws URISyntaxException {
    MeterReadingsDto readingsDto = new MeterReadingsDto(
        "remoteId", "remoteName", "metric", "unit", "readingType",
        new ArrayList<>()
    );

    webhookAdapter.postReadings(readingsDto);

    verify(restTemplate).postForLocation(uriArgumentCaptor.capture(),
        httpEntityArgumentCaptor.capture());

    assertThat(uriArgumentCaptor.getValue()).isEqualTo(energyIdProperties.getSecretUrl().toURI());
    assertThat(httpEntityArgumentCaptor.getValue().getBody()).isEqualTo(readingsDto);
  }

  @Test
  void postReadingsDoesNotRunWhenMockIsTrue() {
    energyIdProperties.setMock(true);

    verify(restTemplate, never()).postForLocation(any(), any());
  }
}