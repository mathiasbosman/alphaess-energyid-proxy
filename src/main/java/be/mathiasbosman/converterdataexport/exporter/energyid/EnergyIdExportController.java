package be.mathiasbosman.converterdataexport.exporter.energyid;

import be.mathiasbosman.converterdataexport.domain.ExportService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Energy ID controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/energyId")
public class EnergyIdExportController {

  private final ExportService exportService;

  /**
   * Endpoint to export weekly PV statistics.
   *
   * @param converterId Unique id of the converter
   * @return {@link ResponseEntity}
   */
  @GetMapping("/trigger/weeklyExport/{converterId}")
  public ResponseEntity<Object> triggerWeeklyExport(
      @PathVariable("converterId") String converterId) {
    LocalDate today = LocalDate.now();
    LocalDate pastWeek = today.minusWeeks(1);
    exportService.exportPvStatisticsForPeriod(converterId, pastWeek, today);
    return ResponseEntity.ok().build();
  }
}
