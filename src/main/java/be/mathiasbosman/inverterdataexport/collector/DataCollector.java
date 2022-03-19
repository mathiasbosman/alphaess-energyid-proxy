package be.mathiasbosman.inverterdataexport.collector;

import be.mathiasbosman.inverterdataexport.domain.PvStatistics;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * Data collector interface.
 */
public interface DataCollector {

  ZoneId getZoneId();

  Optional<PvStatistics> getTotalPv(String inverterId, LocalDate date);

  List<Optional<PvStatistics>> getTotalPvForPeriod(String inverterId, LocalDate start,
      LocalDate end);
}
