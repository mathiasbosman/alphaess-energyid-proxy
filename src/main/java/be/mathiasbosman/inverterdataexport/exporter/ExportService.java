package be.mathiasbosman.inverterdataexport.exporter;

import java.time.LocalDate;

/**
 * Export service interface.
 */
public interface ExportService {

  void exportPvStatisticsForPeriod(String inverterId, LocalDate startDate, LocalDate endDate);
}
