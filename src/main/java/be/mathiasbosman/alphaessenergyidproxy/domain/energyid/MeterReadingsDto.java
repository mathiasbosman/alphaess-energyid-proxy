package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import java.util.List;

public record MeterReadingsDto(
    String remoteId,
    String remoteName,
    String metric,
    String unit,
    String readingType,
    // max 100
    List<List<Object>> data
) {

}
