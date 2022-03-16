package be.mathiasbosman.converterdataexport.collector;

import static org.assertj.core.api.Assertions.assertThat;

import be.mathiasbosman.converterdataexport.PvStatisticStub;
import be.mathiasbosman.converterdataexport.domain.AbstractDataCollector;
import be.mathiasbosman.converterdataexport.domain.DataCollector;
import be.mathiasbosman.converterdataexport.domain.PvStatistics;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AbstractDataCollectorTest {

  private final DataCollector dataCollectorWithoutData = new AbstractDataCollector() {
    @Override
    public ZoneId getZoneId() {
      return null;
    }

    @Override
    public Optional<PvStatistics> getTotalPv(String converterId, LocalDate date) {
      return Optional.empty();
    }
  };

  private final DataCollector dataCollectorWithRandomData = new AbstractDataCollector() {
    @Override
    public ZoneId getZoneId() {
      return null;
    }

    @Override
    public Optional<PvStatistics> getTotalPv(String converterId, LocalDate date) {
      return Optional.of(new PvStatisticStub());
    }
  };

  @Test
  void getTotalPvForPeriod() {
    LocalDate today = LocalDate.now();
    LocalDate threeDaysAgo = today.minusDays(3);
    assertThat(dataCollectorWithRandomData.getTotalPvForPeriod("foo", threeDaysAgo, today))
        .hasSize(3);
  }

  @Test
  void getTotalPvForSameDay() {
    LocalDate date = LocalDate.now();
    assertThat(dataCollectorWithRandomData.getTotalPvForPeriod("foo", date, date))
        .hasSize(1);
  }

  @Test
  void getTotalPvForPeriodWithoutData() {
    LocalDate today = LocalDate.now();
    LocalDate threeDaysAgo = today.minusDays(3);
    assertThat(dataCollectorWithoutData.getTotalPvForPeriod("foo", threeDaysAgo, today))
        .isEmpty();
  }
}
