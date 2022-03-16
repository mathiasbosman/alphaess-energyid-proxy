package be.mathiasbosman.converterdataexport.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Abstract collector holding the method to get a total Pv statistic for a period.
 */
public abstract class AbstractDataCollector implements DataCollector {

  @Override
  public List<PvStatistics> getTotalPvForPeriod(String converterId, LocalDate startDate,
      LocalDate endDate) {
    if (startDate.isEqual(endDate)) {
      return getTotalPv(converterId, startDate)
          .map(Collections::singletonList)
          .orElse(Collections.emptyList());
    }

    return startDate.datesUntil(endDate)
        .map(date -> getTotalPv(converterId, date))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }
}
