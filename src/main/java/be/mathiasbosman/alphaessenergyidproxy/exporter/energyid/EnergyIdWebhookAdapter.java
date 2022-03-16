package be.mathiasbosman.alphaessenergyidproxy.exporter.energyid;

import be.mathiasbosman.alphaessenergyidproxy.exporter.energyid.dto.MeterReadingsDto;

/**
 * Interface for the EnergyID webhook.
 */
public interface EnergyIdWebhookAdapter extends WebhookAdapter<MeterReadingsDto> {

  void postReadings(MeterReadingsDto readingsDto);
}
