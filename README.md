# Solar inverter data exporter

[![Build](https://github.com/mathiasbosman/inverter-data-export/actions/workflows/build.yml/badge.svg)](https://github.com/mathiasbosman/inverter-data-export/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/mathiasbosman/inverter-data-export/branch/master/graph/badge.svg?token=VixDPmMsct)](https://codecov.io/gh/mathiasbosman/inverter-data-export)

This application allows the export of data from a solar inverter.

Currently, only one the [AlphaESS](https://www.alpha-ess.com) inverter (battery) is supported.

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