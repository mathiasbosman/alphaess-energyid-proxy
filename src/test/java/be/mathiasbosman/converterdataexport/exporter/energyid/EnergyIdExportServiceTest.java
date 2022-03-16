package be.mathiasbosman.converterdataexport.exporter.energyid;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.mathiasbosman.converterdataexport.PvStatisticStub;
import be.mathiasbosman.converterdataexport.domain.DataCollector;
import be.mathiasbosman.converterdataexport.exporter.energyid.EnergyIdProperties.EnergyIdMeter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnergyIdExportServiceTest {

  @Mock
  private EnergyIdWebhookAdapter webhookAdapter;
  @Mock
  private DataCollector dataCollector;
  private static final String CONVERTER_ID_1 = "sn1";
  private final EnergyIdProperties energyIdProperties = new EnergyIdProperties();
  private static final String CONVERTER_ID_2 = "sn2";
  private static final LocalDate DATE_NOW = LocalDate.now();
  private static final LocalDate DATE_LAST_WEEK = LocalDate.now().minusWeeks(1);
  private EnergyIdExportService energyIdExportService;

  @BeforeEach
  void initService() {
    energyIdProperties.setMeters(List.of(
        createEnergyIdMeter(CONVERTER_ID_1),
        createEnergyIdMeter(CONVERTER_ID_2)
    ));
    energyIdExportService = new EnergyIdExportService(
        webhookAdapter,
        dataCollector,
        energyIdProperties);
  }

  private EnergyIdMeter createEnergyIdMeter(String converterId) {
    EnergyIdMeter meter = new EnergyIdMeter();
    meter.setMetric("metric");
    meter.setMultiplier(1);
    meter.setReadingType("readingType");
    meter.setRemoteId("remoteId");
    meter.setUnit("unit");
    meter.setConverterId(converterId);
    return meter;
  }

  @Test
  void exportDoesNotTriggerPushWhenNoDataIsCollected() {
    when(dataCollector.getTotalPvForPeriod(any(), any(), any()))
        .thenReturn(Collections.emptyList());

    energyIdExportService.exportPvStatisticsForPeriod(CONVERTER_ID_2, DATE_LAST_WEEK, DATE_NOW);

    verify(webhookAdapter, never()).postReadings(any());
  }

  @Test
  void exportPvStatisticsForPeriod() {
    mockDataCollector();

    energyIdExportService.exportPvStatisticsForPeriod(CONVERTER_ID_1, DATE_LAST_WEEK, DATE_NOW);

    verify(webhookAdapter, times(1)).postReadings(any());
  }

  private void mockDataCollector() {
    when(dataCollector.getZoneId()).thenReturn(ZoneId.of("Europe/Brussels"));
    when(dataCollector.getTotalPvForPeriod(any(), any(), any()))
        .thenReturn(List.of(
            new PvStatisticStub(),
            new PvStatisticStub()
        ));
  }
}