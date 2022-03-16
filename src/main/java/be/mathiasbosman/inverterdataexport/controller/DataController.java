package be.mathiasbosman.inverterdataexport.controller;

import be.mathiasbosman.inverterdataexport.domain.DataCollector;
import be.mathiasbosman.inverterdataexport.domain.PvStatistics;
import java.time.LocalDate;
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
public class DataController {

  private final DataCollector dataCollector;

  /**
   * Returns PV stats for a given date.
   *
   * @param inverterId Unique id of the inverter
   * @param date        The date to query
   * @return {@link PvStatistics}
   */
  @GetMapping("/statistics/pv")
  public ResponseEntity<PvStatistics> getPvStats(String inverterId, LocalDate date) {
    return dataCollector.getTotalPv(inverterId, date)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
