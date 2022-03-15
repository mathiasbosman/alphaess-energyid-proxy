package be.mathiasbosman.alphaessenergyidproxy.domain;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Data collector interface.
 */
public interface DataCollector {

  Optional<PvStatistics> getPvStatistics(String identifier, LocalDateTime date);
}
