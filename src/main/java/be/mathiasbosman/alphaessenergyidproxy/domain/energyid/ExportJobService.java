package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.AlphaessService;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response.SticsByPeriodResponseEntity.Statistics;
import be.mathiasbosman.alphaessenergyidproxy.util.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportJobService {

  private final WebhookAdapter webhookAdapter;
  private final AlphaessService alphaessService;
  private final ProxyProperties proxyProperties;
  private final EnergyIdProperties energyIdProperties;

  @Scheduled(
      cron = "${" + ProxyProperties.PREFIX + ".export-cron}",
      zone = "${" + ProxyProperties.PREFIX + ".timezone}")
  public void collectStatisticsForPastWeek() {
    log.info("Job started for collecting statistics of the past week");
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate pastWeek = yesterday.minusWeeks(1);
    collectStatisticsForPeriod(pastWeek, yesterday);
    log.info("Job ended");
  }

  public void collectStatisticsForPeriod(LocalDate startDate, LocalDate endDate) {
    proxyProperties.getMeters().forEach(meter -> {
      MeterReadingsDto exportDto = MeterReadingsDto.fromEnergyIdMeter(meter);
      startDate.datesUntil(endDate)
          .forEach(date -> addStatsForDate(exportDto, meter.getAlphaSn(), date));
      if (!exportDto.data().isEmpty()) {
        webhookAdapter.postReadings(exportDto);
      }
    });
  }

  private void addStatsForDate(MeterReadingsDto exportDto, String sn, LocalDate pollDate) {
    ZoneId zoneId = ZoneId.of(proxyProperties.getTimezone());
    int maxDataBatch = energyIdProperties.getMaxDataBatchSize();
    LocalDateTime localDateTime = pollDate.atStartOfDay(zoneId).toLocalDateTime();
    String formattedDate = DateUtils.formatISODate(localDateTime, zoneId);
    addStatistics(exportDto, sn, localDateTime, formattedDate);
    if (exportDto.data().size() == maxDataBatch) {
      log.warn("Exceeded max data batch size {}, posting early", maxDataBatch);
      webhookAdapter.postReadings(exportDto);
      exportDto.data().clear();
    }
  }

  private void addStatistics(MeterReadingsDto readingsDto, String sn, LocalDateTime date,
      String dataKey) {
    Optional<Statistics> dailyStatistics = alphaessService.getDailyStatistics(sn, date);
    dailyStatistics.ifPresent(
        stat -> readingsDto.data().add(List.of(dataKey, stat.getPvTotal()))
    );
  }
}
