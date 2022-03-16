package be.mathiasbosman.converterdataexport.domain;

import java.time.LocalDate;

/**
 * Export service interface.
 */
public interface ExportService {

  void exportPvStatisticsForPeriod(String converterId, LocalDate startDate, LocalDate endDate);
}
