package be.mathiasbosman.alphaessenergyidproxy;

import be.mathiasbosman.alphaessenergyidproxy.config.AlphaessProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.EnergyIdMeter;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.AlphaessService;
import be.mathiasbosman.alphaessenergyidproxy.domain.alphaess.response.SticsByPeriodResponseEntity.Statistics;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.MeterReadingDataDto;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.MeterReadingsDto;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportJob {

  private final AlphaessService alphaessService;
  private final ProxyProperties proxyProperties;

  @Scheduled(cron = "${" + ProxyProperties.PREFIX + ".export-cron}",
      zone = "${" + ProxyProperties.PREFIX + ".timezone}")
  void collectStatistics() {
    log.info("Job started for collecting statistics");
    Date today = new Date();
    proxyProperties.getMeters()
        .forEach(meterInfo -> alphaessService.getDailyStatistics(meterInfo.getAlphaSn(), today)
            .ifPresent(statistics -> exportStatistics(meterInfo, statistics, today)
    ));
    log.info("Job ended");
  }

  void exportStatistics(EnergyIdMeter meter, Statistics stats, Date date) {
    //create reading data
    MeterReadingDataDto dataDto = new MeterReadingDataDto(date, stats.getPvTotal());
    MeterReadingsDto exportDto = new MeterReadingsDto(
        meter.getRemoteId(),
        meter.getRemoteName(),
        meter.getMetric(),
        meter.getUnit(),
        meter.getReadingType(),
        Collections.singletonList(dataDto)
    );

    // todo call webhook
  }
}
