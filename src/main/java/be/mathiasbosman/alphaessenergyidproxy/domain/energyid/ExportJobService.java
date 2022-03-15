package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.EnergyIdMeter;
import be.mathiasbosman.alphaessenergyidproxy.domain.DataCollector;
import be.mathiasbosman.alphaessenergyidproxy.domain.ExportService;
import be.mathiasbosman.alphaessenergyidproxy.domain.PvStatistics;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;
import be.mathiasbosman.alphaessenergyidproxy.util.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Service that communicates with the EnergyID webhook.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportJobService implements ExportService {

  private final EnergyIdWebhookAdapter webhookAdapter;
  private final DataCollector dataCollector;
  private final ProxyProperties proxyProperties;

  /**
   * Collects data of the past week and pushes it to the webhook.
   */
  @Scheduled(
      cron = "${" + ProxyProperties.PREFIX + ".export-cron}",
      zone = "${" + ProxyProperties.PREFIX + ".timezone}")
  public void exportStatisticsForPastWeek() {
    LocalDate today = LocalDate.now();
    LocalDate pastWeek = today.minusWeeks(1);
    List<MeterReadingsDto> readings = collectStatisticsForPeriod(pastWeek, today);
    readings.stream()
        .filter(reading -> !reading.data().isEmpty())
        .forEach(webhookAdapter::postReadings);
  }

  List<MeterReadingsDto> collectStatisticsForPeriod(LocalDate startDate, LocalDate endDate) {
    return proxyProperties.getMeters().stream()
        .map(meter -> getReadings(meter, startDate, endDate))
        .toList();
  }

  private MeterReadingsDto getReadings(EnergyIdMeter meter, LocalDate startDate,
      LocalDate endDate) {
    MeterReadingsDto meterReading = MeterReadingsDto.fromEnergyIdMeter(meter);
    String meterId = meter.getAlphaSn();
    log.debug("Collecting statistics for meter {} between {} and {}", meterId, startDate, endDate);
    if (startDate.isEqual(endDate)) {
      log.debug("Start and end date are the same");
      getReading(meterId, startDate)
          .ifPresent(data -> meterReading.data().add(data));
    } else {
      startDate.datesUntil(endDate)
          .forEach(date -> getReading(meterId, date)
              .ifPresent(data -> meterReading.data().add(data)));
    }
    return meterReading;
  }

  private Optional<List<Object>> getReading(String meterId, LocalDate date) {
    ZoneId zoneId = ZoneId.of(proxyProperties.getTimezone());
    LocalDateTime timeAtStartOfDay = DateUtils.atStartOfDayInZone(date, zoneId);
    Optional<PvStatistics> stats = dataCollector.getPvStatistics(meterId, timeAtStartOfDay);
    List<Object> result = new ArrayList<>();

    stats.ifPresent(stat -> {
      String formattedIsoDate = DateUtils.formatAsIsoDate(timeAtStartOfDay, zoneId);
      result.add(formattedIsoDate);
      result.add(stat.getPvTotal());
    });

    return !result.isEmpty() ? Optional.of(result) : Optional.empty();
  }
}
