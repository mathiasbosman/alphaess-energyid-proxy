package be.mathiasbosman.alphaessenergyidproxy.exporter.energyid.dto;

import java.time.LocalDateTime;

/**
 * Single data point of a reading. Currently, unused as the EnergyID webhook just takes an array of
 * arrays.
 */
@SuppressWarnings("unused")
public record MeterReadingDataDto(LocalDateTime date, double reading) {

}
