package be.mathiasbosman.alphaessexport.controller;

import be.mathiasbosman.alphaessexport.domain.DataCollector;
import be.mathiasbosman.alphaessexport.domain.ExportService;
import be.mathiasbosman.alphaessexport.domain.PvStatistics;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to manually trigger an export.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class ProxyController {

  private final DataCollector dataCollector;
  private final ExportService exportService;

  @GetMapping("/trigger/weeklyExport")
  public ResponseEntity<Object> triggerWeeklyExport() {
    exportService.exportStatisticsForPastWeek();
    return ResponseEntity.ok().build();
  }

  @GetMapping("/statistics/pv")
  public ResponseEntity<PvStatistics> getPvStats(String sourceIdentifier, LocalDateTime date) {
    return dataCollector.getPvStatistics(sourceIdentifier, date)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
