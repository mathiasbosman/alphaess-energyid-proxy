package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.config.ProxyProperties.EnergyIdMeter;
import java.util.ArrayList;
import java.util.List;

public record MeterReadingsDto(
    String remoteId,
    String remoteName,
    String metric,
    String unit,
    String readingType,
    List<List<Object>> data
) {

  public static MeterReadingsDto fromEnergyIdMeter(EnergyIdMeter energyIdMeter) {
    return new MeterReadingsDto(
        energyIdMeter.getRemoteId(),
        energyIdMeter.getRemoteName(),
        energyIdMeter.getMetric(),
        energyIdMeter.getUnit(),
        energyIdMeter.getReadingType(),
        new ArrayList<>()
    );
  }
}
