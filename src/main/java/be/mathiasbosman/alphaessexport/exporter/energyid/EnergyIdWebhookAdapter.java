package be.mathiasbosman.alphaessexport.exporter.energyid;

import be.mathiasbosman.alphaessexport.exporter.energyid.dto.MeterReadingsDto;

/**
 * Interface for the EnergyID webhook.
 */
public interface EnergyIdWebhookAdapter extends WebhookAdapter<MeterReadingsDto> {

  void postReadings(MeterReadingsDto readingsDto);
}
