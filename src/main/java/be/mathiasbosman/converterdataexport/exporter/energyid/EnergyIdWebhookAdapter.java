package be.mathiasbosman.converterdataexport.exporter.energyid;

import be.mathiasbosman.converterdataexport.exporter.energyid.dto.MeterReadingsDto;

/**
 * Interface for the EnergyID webhook.
 */
public interface EnergyIdWebhookAdapter extends WebhookAdapter<MeterReadingsDto> {

  void postReadings(MeterReadingsDto readingsDto);
}
