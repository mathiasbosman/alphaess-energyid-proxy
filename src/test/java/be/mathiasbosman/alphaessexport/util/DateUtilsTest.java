package be.mathiasbosman.alphaessexport.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

  @Test
  void formatISODate() {
    ZoneId brussels = ZoneId.of("Europe/Brussels");
    ZoneId losAngeles = ZoneId.of("America/Los_Angeles");
    LocalDate localDate = LocalDate.of(2020, 3, 1);

    assertThat(DateUtils.formatAsIsoDate(
        localDate.atStartOfDay().atZone(brussels).toLocalDateTime(), brussels
    )).isEqualTo("2020-03-01T00:00:00+01:00");

    assertThat(DateUtils.formatAsIsoDate(
        localDate.atStartOfDay().atZone(losAngeles).toLocalDateTime(), losAngeles
    )).isEqualTo("2020-03-01T00:00:00-08:00");
  }

  @Test
  void atStartOfDayInZone() {
    ZoneId brussels = ZoneId.of("Europe/Brussels");
    LocalDate localDate = LocalDate.now();

    assertThat(DateUtils.atStartOfDayInZone(localDate, brussels))
        .isInstanceOf(LocalDateTime.class)
        .satisfies(dateTime -> {
          assertThat(dateTime.getHour()).isZero();
          assertThat(dateTime.getMinute()).isZero();
          assertThat(dateTime.getSecond()).isZero();
        });
  }
}