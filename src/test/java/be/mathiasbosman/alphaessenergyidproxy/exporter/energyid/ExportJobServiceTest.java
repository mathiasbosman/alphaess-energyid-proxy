package be.mathiasbosman.alphaessenergyidproxy.exporter.energyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.mathiasbosman.alphaessenergyidproxy.config.AlphaessProperties;
import be.mathiasbosman.alphaessenergyidproxy.domain.DataCollector;
import be.mathiasbosman.alphaessenergyidproxy.exporter.energyid.EnergyIdProperties.EnergyIdMeter;
import be.mathiasbosman.alphaessenergyidproxy.exporter.energyid.dto.MeterReadingsDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExportJobServiceTest {

  @Mock
  private EnergyIdWebhookAdapter webhookAdapter;
  @Mock
  private DataCollector dataCollector;
  private ExportJobService exportJobService;
  private final EnergyIdProperties energyIdProperties = new EnergyIdProperties();
  private final AlphaessProperties alphaessProperties = new AlphaessProperties();

  @BeforeEach
  void initService() {
    energyIdProperties.setMeters(List.of(
        createEnergyIdMeter("sn1"),
        createEnergyIdMeter("sn2")
    ));
    exportJobService = new ExportJobService(
        webhookAdapter,
        dataCollector,
        energyIdProperties,
        alphaessProperties);
  }

  private EnergyIdMeter createEnergyIdMeter(String alphaSn) {
    EnergyIdMeter meter = new EnergyIdMeter();
    meter.setMetric("metric");
    meter.setMultiplier(1);
    meter.setReadingType("readingType");
    meter.setRemoteId("remoteId");
    meter.setUnit("unit");
    meter.setAlphaSn(alphaSn);
    return meter;
  }

  @Test
  void exportJobDoesNotTriggerPushWhenNoDataIsCollected() {
    when(dataCollector.getPvStatistics(any(), any()))
        .thenReturn(Optional.empty());

    exportJobService.exportStatisticsForPastWeek();

    verify(webhookAdapter, never()).postReadings(any());
  }

  @Test
  void exportStatisticsForPastWeekJob() {
    mockDataCollector();

    exportJobService.exportStatisticsForPastWeek();

    verify(webhookAdapter, times(energyIdProperties.getMeters().size())).postReadings(any());
  }

  @Test
  void collectStatisticsForSameDay() {
    LocalDate date = LocalDate.now();
    mockDataCollector();

    List<MeterReadingsDto> data = exportJobService.collectStatisticsForPeriod(date, date);

    assertThat(data).hasSize(energyIdProperties.getMeters().size());
  }

  @Test
  void collectStatisticsForPeriod() {
    int dayDiff = 12;
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(dayDiff);

    mockDataCollector();

    List<MeterReadingsDto> data = exportJobService.collectStatisticsForPeriod(startDate, endDate);

    assertThat(data)
        .hasSize(2)
        .satisfies(meterReadingsDto ->
            meterReadingsDto.forEach(reading -> assertThat(reading.data()).hasSize(12))
        );
  }

  private void mockDataCollector() {
    when(dataCollector.getPvStatistics(any(), any()))
        .thenReturn(Optional.of(() -> {
          Random random = new Random();
          return random.nextDouble();
        }));
  }
}