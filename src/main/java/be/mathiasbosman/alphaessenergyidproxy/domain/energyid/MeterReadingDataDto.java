package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import java.time.LocalDateTime;

public record MeterReadingDataDto(LocalDateTime date, double reading) {

}
