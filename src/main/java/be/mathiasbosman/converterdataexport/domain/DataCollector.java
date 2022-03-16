package be.mathiasbosman.converterdataexport.domain;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * Data collector interface.
 */
public interface DataCollector {

  ZoneId getZoneId();

  Optional<PvStatistics> getTotalPv(String converterId, LocalDate date);

  List<PvStatistics> getTotalPvForPeriod(String converterId, LocalDate start, LocalDate end);
}
