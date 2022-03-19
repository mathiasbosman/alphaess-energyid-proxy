package be.mathiasbosman.inverterdataexport.exporter.energyid;

import be.mathiasbosman.inverterdataexport.domain.DataCollector;
import be.mathiasbosman.inverterdataexport.domain.ExportService;
import be.mathiasbosman.inverterdataexport.domain.ExporterException;
import be.mathiasbosman.inverterdataexport.domain.PvStatistics;
import be.mathiasbosman.inverterdataexport.exporter.energyid.EnergyIdProperties.EnergyIdMeter;
import be.mathiasbosman.inverterdataexport.exporter.energyid.dto.MeterReadingsDto;
import be.mathiasbosman.inverterdataexport.util.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

/**
 * Service that communicates with the EnergyID webhook.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnergyIdExportService implements ExportService {

  private final EnergyIdWebhookAdapter webhookAdapter;
  private final DataCollector dataCollector;
  private final EnergyIdProperties energyIdProperties;

  /**
   * Exports statistics to the EnergyID platform for a given period.
   *
   * @param inverterId The unique id of the inverter
   * @param start       Start date
   * @param end         End date (not inclusive)
   */
  public void exportPvStatisticsForPeriod(String inverterId, LocalDate start, LocalDate end) {
    EnergyIdMeter energyIdMeter = getEnergyIdMeter(inverterId);
    List<PvStatistics> statistics = dataCollector.getTotalPvForPeriod(inverterId, start, end);
    ZoneId zoneId = dataCollector.getZoneId();

    MeterReadingsDto meterReadingsDto = MeterReadingsDto.fromEnergyIdMeter(energyIdMeter);
    statistics.forEach(pvStatistics -> {
      LocalDateTime localDateTime = DateUtils.atStartOfDayInZone(pvStatistics.getDate(), zoneId);
      List<Object> data = List.of(
          DateUtils.formatAsIsoDate(localDateTime, zoneId),
          pvStatistics.getPvTotal());
      meterReadingsDto.data().add(data);
    });

    if (!meterReadingsDto.data().isEmpty()) {
      webhookAdapter.postReadings(meterReadingsDto);
    }
  }

  private EnergyIdMeter getEnergyIdMeter(String inverterId) {
    return energyIdProperties.getMeters().stream()
        .filter(meter -> meter.getInverterId().equals(inverterId))
        .findFirst()
        .orElseThrow(() -> new ExporterException(Level.ERROR, "No meter found for %s", inverterId));
  }
}
