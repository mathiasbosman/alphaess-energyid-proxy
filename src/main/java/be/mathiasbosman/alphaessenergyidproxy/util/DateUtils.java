package be.mathiasbosman.alphaessenergyidproxy.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public static String formatISODate(LocalDateTime localDateTime, ZoneId zoneId) {
    ZoneOffset zoneOffset = zoneId.getRules().getOffset(localDateTime);
    return localDateTime.atOffset(zoneOffset).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
  }
}
