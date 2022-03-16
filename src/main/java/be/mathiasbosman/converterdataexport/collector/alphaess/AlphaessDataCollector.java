package be.mathiasbosman.converterdataexport.collector.alphaess;

import be.mathiasbosman.converterdataexport.collector.alphaess.dto.LoginDto;
import be.mathiasbosman.converterdataexport.collector.alphaess.dto.StatisticsDto;
import be.mathiasbosman.converterdataexport.collector.alphaess.response.LoginResponseEntity;
import be.mathiasbosman.converterdataexport.collector.alphaess.response.LoginResponseEntity.LoginData;
import be.mathiasbosman.converterdataexport.collector.alphaess.response.SticsByPeriodResponseEntity;
import be.mathiasbosman.converterdataexport.collector.alphaess.response.SticsByPeriodResponseEntity.Statistics;
import be.mathiasbosman.converterdataexport.domain.AbstractDataCollector;
import be.mathiasbosman.converterdataexport.domain.PvStatistics;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service to integrate with the AlphaESS API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlphaessDataCollector extends AbstractDataCollector {

  private final RestTemplate restTemplate;
  private final AlphaessProperties alphaessProperties;


  private LoginData loginData;

  private HttpHeaders buildHeaders(boolean isSecure) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setConnection("keep-alive");
    headers.setCacheControl(CacheControl.noCache());
    if (isSecure) {
      if (loginData == null || !isTokenValid(loginData)) {
        loginData = authenticate(LoginDto.fromCredentials(alphaessProperties.getCredentials()));
      }
      headers.setBearerAuth(loginData.getAccessToken());
    }
    return headers;
  }

  URI buildUri(String path) {
    return UriComponentsBuilder.fromHttpUrl(alphaessProperties.getBaseUrl().toExternalForm())
        .path(path)
        .build()
        .toUri();
  }

  /**
   * Authenticate with the API.
   *
   * @param loginDto Used credentials
   * @return {@link LoginData}
   */
  public LoginData authenticate(LoginDto loginDto) {
    URI uri = buildUri(alphaessProperties.getEndpoints().getAuthentication());
    log.trace("Authenticating on {}", uri);
    HttpEntity<LoginDto> request = new HttpEntity<>(loginDto, buildHeaders(false));
    LoginResponseEntity responseEntity = restTemplate.postForObject(uri, request,
        LoginResponseEntity.class);
    if (responseEntity == null) {
      throw new IllegalStateException("Response entity is null");
    }
    return responseEntity.getData();
  }

  boolean isTokenValid(@NonNull LoginData loginData) {
    if (loginData.getAccessToken() != null
        && loginData.getExpiresIn() != 0
        && loginData.getTokenCreateTime() != null) {
      Date date = new Date();
      long diffInMillies = Math.abs(date.getTime() - loginData.getTokenCreateTime().getTime());
      long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
      return diff < loginData.getExpiresIn();
    }
    return false;
  }

  @Override
  public ZoneId getZoneId() {
    return ZoneId.of(alphaessProperties.getTimezone());
  }

  /**
   * Gets the AlphaESS statistics for a given serial number on a given day.
   *
   * @param serial The serial number
   * @param date   The date as {@link LocalDateTime}
   * @return Optional of {@link Statistics}
   */
  @Override
  public Optional<PvStatistics> getTotalPv(@NonNull String serial, @NonNull LocalDate date) {
    URI uri = buildUri(alphaessProperties.getEndpoints().getDailyStats());
    log.trace("Getting statistics on {} for {} on {}", uri, serial, date);
    LocalDateTime startDay = date.atStartOfDay();
    LocalDateTime endDay = date.atTime(LocalTime.MAX);
    StatisticsDto statisticsDto = new StatisticsDto(startDay, endDay, LocalDate.now(), 0, serial,
        "", true);
    HttpEntity<StatisticsDto> request = new HttpEntity<>(statisticsDto, buildHeaders(true));
    SticsByPeriodResponseEntity result = restTemplate.postForObject(uri, request,
        SticsByPeriodResponseEntity.class);
    if (result == null || result.getData() == null) {
      return Optional.empty();
    }

    return Optional.of(
        new PvStatistics() {
          @Override
          public LocalDate getDate() {
            return date;
          }

          @Override
          public double getPvTotal() {
            return result.getData().getPvTotal();
          }
        }
    );

  }
}
