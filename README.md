# Solar inverter data exporter

[![Build](https://github.com/mathiasbosman/inverter-data-export/actions/workflows/build.yml/badge.svg)](https://github.com/mathiasbosman/inverter-data-export/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mathiasbosman_inverter-data-export&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mathiasbosman_inverter-data-export)

This application allows the export of data from a solar inverter.

Currently, only the [AlphaESS](https://www.alpha-ess.com) inverter (battery) is supported.

Exporting is currently only supported to the [EnergyID](https://energyid.eu) platform.

## Configuration

Depending on the needs of the data collectors and exporters different configuration is required.

### Data collectors

#### AlphaESS API

To connect to the AlphaESS you need to set your user credentials, the timezone the inverter runs in
and the API endpoints.

```yaml
alphaess:
  base-url: "https://www.alphaess.com/api"
  endpoints:
    authentication: "/Account/Login"
    daily-stats: "/Power/SticsByPeriod"
  credentials:
    username: "foo"
    password: "bar"
  timezone: "Europe/Brussels"
```
(these are also the default values

### Exporters

#### EnergyID (webhook)

To connect the EnergyID webhook you need the provided secret url and set it as such. In addition,
set the maximum batch size.

This is also the place to configure your meters in EnergyID. Combine them with the identifier of the
inverter.

````yaml
energyid:
  secret-url: "https://hooks.energyid.eu/services/WebhookIn/xxxxx-xxx/..."
  max-data-batch-size: 100
  meters:
    - inverterId: "FOO_BAR"
      remoteId: "FOO_BAR"
      remoteName: "FOO_BAR"
      metric: "solarPhotovoltaicProduction"
      unit: "kWh"
      readingType: "premarkedInterval"
````