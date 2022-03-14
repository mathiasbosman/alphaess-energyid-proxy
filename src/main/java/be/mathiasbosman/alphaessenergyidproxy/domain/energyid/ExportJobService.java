package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.EnergyIdMeter;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.AlphaessService;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response.SticsByPeriodResponseEntity.Statistics;
import be.mathiasbosman.alphaessenergyidproxy.util.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
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

  @Scheduled(cron = "${" + ProxyProperties.PREFIX + ".export-cron}",
      zone = "${" + ProxyProperties.PREFIX + ".timezone}")
  public void collectStatistics() {
    log.info("Job started for collecting statistics");
    LocalDateTime localDateTime = LocalDate.now()
        .atStartOfDay(ZoneId.of(proxyProperties.getTimezone()))
        .toLocalDateTime();
    collectStatisticsForDate(localDateTime);
    log.info("Job ended");
  }

  public void collectStatisticsForDate(LocalDateTime date) {
    proxyProperties.getMeters()
        .forEach(meterInfo -> alphaessService.getDailyStatistics(meterInfo.getAlphaSn(), date)
            .ifPresent(statistics -> exportStatistics(meterInfo, statistics, date)));
  }

  public void collectStatisticsForPeriod(LocalDate startDate, LocalDate endDate, int maxDataBatch) {
    ZoneId zoneId = ZoneId.of(proxyProperties.getTimezone());
    proxyProperties.getMeters().forEach(meter -> {
      MeterReadingsDto exportDto = new MeterReadingsDto(
          meter.getRemoteId(),
          meter.getRemoteName(),
          meter.getMetric(),
          meter.getUnit(),
          meter.getReadingType(),
          new ArrayList<>()
      );
      // get stats for every day
      for (LocalDate pollDate = startDate; pollDate.isBefore(endDate);
          pollDate = pollDate.plusDays(1)) {
        LocalDateTime localDateTime = pollDate.atStartOfDay(zoneId).toLocalDateTime();
        String formattedDate = DateUtils.formatISODate(localDateTime, zoneId);
        Optional<Statistics> dailyStatistics = alphaessService.getDailyStatistics(
            meter.getAlphaSn(), localDateTime);
        dailyStatistics.ifPresent(
            stat -> exportDto.data().add(List.of(formattedDate, stat.getPvTotal()))
        );

        if (exportDto.data().size() == maxDataBatch) {
          log.warn("Exceeded max data batch size {}, posting early", maxDataBatch);
          webhookAdapter.postReadings(exportDto);
          exportDto.data().clear();
        }
      }
      if (!exportDto.data().isEmpty()) {
        webhookAdapter.postReadings(exportDto);
      }
    });
  }

  void exportStatistics(EnergyIdMeter meter, Statistics stats, LocalDateTime date) {
    //create reading data at start of day
    MeterReadingDataDto dataDto = new MeterReadingDataDto(date, stats.getPvTotal());

    ZoneId zoneId = ZoneId.of(proxyProperties.getTimezone());
    String formattedDate = DateUtils.formatISODate(date, zoneId);
    // convert dataDto to list of objects
    List<Object> data = List.of(formattedDate, dataDto.reading());
    // create export object to post
    MeterReadingsDto exportDto = new MeterReadingsDto(
        meter.getRemoteId(),
        meter.getRemoteName(),
        meter.getMetric(),
        meter.getUnit(),
        meter.getReadingType(),
        Collections.singletonList(data)
    );

    webhookAdapter.postReadings(exportDto);
  }
}
