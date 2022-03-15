package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.mathiasbosman.alphaessenergyidproxy.config.EnergyIdProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties;
import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.EnergyIdMeter;
import be.mathiasbosman.alphaessenergyidproxy.domain.DataCollector;
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

  private static final int maxBatchSize = 5;
  private final ProxyProperties proxyProperties = new ProxyProperties();
  private final EnergyIdProperties energyIdProperties = new EnergyIdProperties();
  @Mock
  private EnergyIdWebhookAdapter webhookAdapter;
  @Mock
  private DataCollector dataCollector;
  private ExportJobService exportJobService;

  @BeforeEach
  void initService() {
    energyIdProperties.setMaxDataBatchSize(maxBatchSize);
    proxyProperties.setMeters(List.of(
        createEnergyIdMeter("sn1"),
        createEnergyIdMeter("sn2")
    ));
    exportJobService = new ExportJobService(
        webhookAdapter,
        dataCollector,
        proxyProperties,
        energyIdProperties);
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
  void collectStatisticsForPastWeekDoesNotTriggerPushWhenNoDataIsCollected() {
    when(dataCollector.getPvStatistics(any(), any()))
        .thenReturn(Optional.empty());

    exportJobService.collectStatisticsForPastWeek();

    verify(webhookAdapter, never()).postReadings(any());
  }

  @Test
  void collectStatisticsTriggersMultipleBatchesIfNeeded() {
    int dayDiff = 12;
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(dayDiff);

    when(dataCollector.getPvStatistics(any(), any()))
        .thenReturn(Optional.of(() -> {
          Random random = new Random();
          return random.nextDouble();
        }));

    exportJobService.collectStatisticsForPeriod(startDate, endDate);

    // 12 days => 24 records => 2 batches of 5 + 1 of 4
    int fullBatchesPerMeter = dayDiff / maxBatchSize;
    int batchesPerMeter = fullBatchesPerMeter + 1; // always rest records because of the 12 days
    int expectedAmountOfPosts = proxyProperties.getMeters().size() * batchesPerMeter;
    verify(webhookAdapter, times(expectedAmountOfPosts)).postReadings(any());
  }

  @Test
  void collectStatisticsInOneBatchIfBatchSizeSetToZero() {
    energyIdProperties.setMaxDataBatchSize(0);
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(33);

    when(dataCollector.getPvStatistics(any(), any()))
        .thenReturn(Optional.of(() -> {
          Random random = new Random();
          return random.nextDouble();
        }));

    exportJobService.collectStatisticsForPeriod(startDate, endDate);

    verify(webhookAdapter, times(proxyProperties.getMeters().size())).postReadings(any());
  }
}