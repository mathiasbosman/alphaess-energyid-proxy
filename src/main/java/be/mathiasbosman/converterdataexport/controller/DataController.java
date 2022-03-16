package be.mathiasbosman.converterdataexport.controller;

import be.mathiasbosman.converterdataexport.domain.DataCollector;
import be.mathiasbosman.converterdataexport.domain.PvStatistics;
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
   * @param converterId Unique id of the converter
   * @param date        The date to query
   * @return {@link PvStatistics}
   */
  @GetMapping("/statistics/pv")
  public ResponseEntity<PvStatistics> getPvStats(String converterId, LocalDate date) {
    return dataCollector.getTotalPv(converterId, date)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
