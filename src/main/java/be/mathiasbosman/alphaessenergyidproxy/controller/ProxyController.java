package be.mathiasbosman.alphaessenergyidproxy.controller;

import be.mathiasbosman.alphaessenergyidproxy.domain.DataCollector;
import be.mathiasbosman.alphaessenergyidproxy.domain.ExportService;
import be.mathiasbosman.alphaessenergyidproxy.domain.PvStatistics;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.EnergyIdWebhookAdapter;
import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final EnergyIdWebhookAdapter webhookAdapter;

  @GetMapping("/trigger/weeklyExport")
  public ResponseEntity<Object> triggerWeeklyExport() {
    exportService.exportStatisticsForPastWeek();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/export")
  public ResponseEntity<Object> exportData(MeterReadingsDto dto) {
    webhookAdapter.postReadings(dto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/statistics/pv")
  public ResponseEntity<PvStatistics> getPvStats(String sourceIdentifier, LocalDateTime date) {
    return dataCollector.getPvStatistics(sourceIdentifier, date)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
