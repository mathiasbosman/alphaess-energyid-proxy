package be.mathiasbosman.alphaessenergyidproxy.domain.alphaess;

import be.mathiasbosman.alphaessenergyidproxy.config.AlphaessProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StatisticsDto(
    @JsonFormat(pattern = AlphaessProperties.DATE_FORMAT_PATTERN) LocalDateTime beginDay,
    @JsonFormat(pattern = AlphaessProperties.DATE_FORMAT_PATTERN) LocalDateTime endDay,
    @JsonFormat(pattern = AlphaessProperties.DATE_FORMAT_PATTERN) LocalDate tDay,
    int isOEM,
    String SN,
    String userId,
    boolean noLoading) {

}
