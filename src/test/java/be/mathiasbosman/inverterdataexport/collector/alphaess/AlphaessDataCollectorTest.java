package be.mathiasbosman.inverterdataexport.collector.alphaess;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import be.mathiasbosman.inverterdataexport.collector.alphaess.AlphaessProperties.Credentials;
import be.mathiasbosman.inverterdataexport.collector.alphaess.AlphaessProperties.Endpoints;
import be.mathiasbosman.inverterdataexport.collector.alphaess.dto.LoginDto;
import be.mathiasbosman.inverterdataexport.collector.alphaess.response.LoginResponseEntity;
import be.mathiasbosman.inverterdataexport.collector.alphaess.response.LoginResponseEntity.LoginData;
import be.mathiasbosman.inverterdataexport.collector.alphaess.response.SticsByPeriodResponseEntity;
import be.mathiasbosman.inverterdataexport.collector.alphaess.response.SticsByPeriodResponseEntity.Statistics;
import be.mathiasbosman.inverterdataexport.domain.PvStatistics;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AlphaessDataCollectorTest {

  @Mock
  private RestTemplate restTemplate;
  private final AlphaessProperties alphaessProperties = new AlphaessProperties();
  private final Endpoints endpoints = new Endpoints();

  private static final String timeZone = "Europe/Brussels";
  private AlphaessDataCollector alphaessDataCollector;

  @BeforeEach
  void initService() throws MalformedURLException {
    alphaessProperties.setBaseUrl(new URL("https://foo"));
    alphaessProperties.setTimezone(timeZone);
    endpoints.setAuthentication("/auth");
    endpoints.setDailyStats("/stats");
    alphaessProperties.setEndpoints(endpoints);
    alphaessDataCollector = new AlphaessDataCollector(restTemplate, alphaessProperties);
  }

  @Test
  void buildUri() throws URISyntaxException {
    assertThat(alphaessDataCollector.buildUri("bar")).isEqualTo(new URI("https://foo/bar"));
  }

  @Test
  void authentication() {
    LoginData mockedData = mockAuth();

    LoginData authenticate = alphaessDataCollector.authenticate(new LoginDto("foo", "bar"));

    assertThat(authenticate).isEqualTo(mockedData);
  }

  @Test
  void authenticationFails() {
    when(restTemplate.postForObject(any(), any(), eq(LoginResponseEntity.class)))
        .thenReturn(null);

    LoginDto loginDto = new LoginDto("foo", "bar");
    assertThrows(IllegalStateException.class,
        () -> alphaessDataCollector.authenticate(loginDto));
  }

  @Test
  void isTokenValid() {
    LoginData missingToken = LoginData.builder().accessToken(null).build();
    LoginData expiresInIsZero = LoginData.builder().accessToken("123").expiresIn(0).build();
    LoginData creationTimeIsNull = LoginData.builder().accessToken("123").expiresIn(10)
        .tokenCreateTime(null).build();
    LoginData validToken = LoginData.builder().accessToken("123").expiresIn(1000)
        .tokenCreateTime(new Date()).build();

    assertThat(alphaessDataCollector.isTokenValid(missingToken)).isFalse();
    assertThat(alphaessDataCollector.isTokenValid(expiresInIsZero)).isFalse();
    assertThat(alphaessDataCollector.isTokenValid(creationTimeIsNull)).isFalse();
    assertThat(alphaessDataCollector.isTokenValid(validToken)).isTrue();
  }

  @Test
  void getTotalPv() {
    mockAuth();
    SticsByPeriodResponseEntity response = new SticsByPeriodResponseEntity();
    response.setData(Statistics.builder().pvTotal(50).build());
    when(restTemplate.postForObject(any(), any(), eq(SticsByPeriodResponseEntity.class)))
        .thenReturn(response);

    Optional<PvStatistics> stats = alphaessDataCollector.getTotalPv("sn", LocalDate.now());
    assertThat(stats).isNotEmpty();
    assertThat(stats.get().getPvTotal()).isEqualTo(50);
  }

  @Test
  void getTotalPvIsEmpty() {
    mockAuth();
    when(restTemplate.postForObject(any(), any(), eq(SticsByPeriodResponseEntity.class)))
        .thenReturn(null);

    assertThat(alphaessDataCollector.getTotalPv("123", LocalDate.now())).isEmpty();
  }

  private LoginData mockAuth() {
    Credentials credentials = new Credentials();
    credentials.setUsername("foo");
    credentials.setPassword("bar");
    alphaessProperties.setCredentials(credentials);
    LoginData loginData = LoginData.builder().accessToken("123").build();
    LoginResponseEntity responseEntity = new LoginResponseEntity();
    responseEntity.setData(loginData);

    when(restTemplate.postForObject(any(), any(), eq(LoginResponseEntity.class)))
        .thenReturn(responseEntity);

    return loginData;
  }

  @Test
  void getTimeZone() {
    assertThat(alphaessDataCollector.getZoneId()).isEqualTo(ZoneId.of(timeZone));
  }
}