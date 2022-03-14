package be.mathiasbosman.alphaessenergyidproxy.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

  @Test
  void formatISODate() {
    ZoneId brussels = ZoneId.of("Europe/Brussels");
    ZoneId losAngeles = ZoneId.of("America/Los_Angeles");
    LocalDate localDate = LocalDate.of(2020, 3, 1);

    assertThat(DateUtils.formatISODate(
        localDate.atStartOfDay().atZone(brussels).toLocalDateTime(), brussels
    )).isEqualTo("2020-03-01T00:00:00+01:00");

    assertThat(DateUtils.formatISODate(
        localDate.atStartOfDay().atZone(losAngeles).toLocalDateTime(), losAngeles
    )).isEqualTo("2020-03-01T00:00:00-08:00");
  }

}