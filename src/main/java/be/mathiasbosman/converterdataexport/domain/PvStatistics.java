package be.mathiasbosman.converterdataexport.domain;

import java.time.LocalDate;

/**
 * PvStatistics interface.
 */
public interface PvStatistics {

  LocalDate getDate();

  double getPvTotal();
}
