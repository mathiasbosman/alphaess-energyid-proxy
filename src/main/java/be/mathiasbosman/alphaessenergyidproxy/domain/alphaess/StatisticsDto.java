package be.mathiasbosman.alphaessenergyidproxy.domain.alphaess;

import be.mathiasbosman.alphaessenergyidproxy.config.AlphaessProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public record StatisticsDto(
    @JsonFormat(pattern = AlphaessProperties.DATE_FORMAT_PATTERN) Date beginDay,
    @JsonFormat(pattern = AlphaessProperties.DATE_FORMAT_PATTERN) Date endDay,
    @JsonFormat(pattern = AlphaessProperties.DATE_FORMAT_PATTERN) Date tDay,
    int isOEM,
    String SN,
    String userId,
    boolean noLoading) {

}
