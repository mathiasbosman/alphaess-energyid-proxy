# AlphaESS to EnergyID proxy

[![Build](https://github.com/mathiasbosman/alphaess-energyid-proxy/actions/workflows/build.yml/badge.svg)](https://github.com/mathiasbosman/alphaess-energyid-proxy/actions/workflows/build.yml)

This proxy uses the EnergyID webhook to import data from the AlphaESS battery/inverter.

It will take all date of the current date and post it to the EnergyID webhook. This is done via a
cron-job.

## Configuration

Both the connection to the AlphaESS API and the EnergyID webhook need to be configured. If need be
multiple meters can be linked to multiple or singular AlphaESS systems.

### AlphaESS API

To connect to the AlphaESS you need to set your username and password:

```yaml
alphaess:
  credentials:
    username: "JohnDoe"
    password: "Area51"
```

### EnergyID (webhook)

To connect the EnergyID webhook you need the provided secret url and set it as such:

````yaml
energyid:
  secret-url: "https://hooks.energyid.eu/services/WebhookIn/4a0a4e77-33ff-4fb0-9535-155ad7af20f9/OJ7SO8YMVB8Q"

````

### Proxy configuration

To combine you need to set each EnergyID meter and link it up to a serial number of the AlphaESS
system. Below are two examples:

```yaml
proxy:
  meters:
    - alphaSn: "ALSERIALNUMBER00001"
      remoteId: "solarAlphaEss001"
      remoteName: "solarAlphaEss001"
      metric: "solarPhotovoltaicProduction"
      unit: "kWh"
      readingType: "premarkedInterval"
    - alphaSn: "ALSERIALNUMBER00002"
      remoteId: "solarAlphaEss002"
      remoteName: "solarAlphaEss002"
      metric: "solarPhotovoltaicProduction"
      unit: "kWh"
      readingType: "premarkedInterval"
```

**Be aware that all readings will be marked as read at 00:00 of the current day.**

If need be the cron job can be configured although, it is advised to let it run as late as possible.

````yaml
proxy:
  timezone: "Europe/Brussels"
  export-cron: "0 50 23 * * ?"
````