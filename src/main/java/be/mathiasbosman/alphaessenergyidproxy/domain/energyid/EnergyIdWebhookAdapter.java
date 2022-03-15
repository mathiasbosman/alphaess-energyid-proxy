package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;

/**
 * Interface for the EnergyID webhook.
 */
public interface EnergyIdWebhookAdapter {

  void postReadings(MeterReadingsDto readingsDto);
}
