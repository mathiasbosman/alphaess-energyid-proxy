package be.mathiasbosman.alphaessenergyidproxy.domain.energyid;

import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.dto.MeterReadingsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class WebhookAdapterStub implements EnergyIdWebhookAdapter {

  @Autowired
  private ObjectMapper mapper;

  @Override
  @SneakyThrows
  public void postReadings(MeterReadingsDto readingsDto) {
    log.debug("Stub would post: {}", mapper.writeValueAsString(readingsDto));
  }
}
