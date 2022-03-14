package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.EnergyIdMeter;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.AlphaessService;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response.SticsByPeriodResponseEntity.Statistics;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportJob {

  private final WebhookAdapter webhookAdapter;
  private final AlphaessService alphaessService;
  private final ProxyProperties proxyProperties;

  @Scheduled(cron = "${" + ProxyProperties.PREFIX + ".export-cron}",
      zone = "${" + ProxyProperties.PREFIX + ".timezone}")
  void collectStatistics() {
    log.info("Job started for collecting statistics");
    Date today = new Date();
    proxyProperties.getMeters()
        .forEach(meterInfo -> alphaessService.getDailyStatistics(meterInfo.getAlphaSn(), today)
            .ifPresent(statistics -> exportStatistics(meterInfo, statistics, today)));
    log.info("Job ended");
  }

  void exportStatistics(EnergyIdMeter meter, Statistics stats, Date date) {
    //create reading data at start of day
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    MeterReadingDataDto dataDto = new MeterReadingDataDto(localDate.atStartOfDay(),
        stats.getPvTotal());
    // convert dataDto to list of objects
    List<Object> data = List.of(dataDto.date(), dataDto.reading());
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
