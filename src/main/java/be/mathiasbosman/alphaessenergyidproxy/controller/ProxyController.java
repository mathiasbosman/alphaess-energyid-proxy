package be.mathiasbosman.alphaessenergyidproxy.controller;

import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.ExportJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProxyController {

  private final ExportJobService exportJobService;

  @GetMapping("/rest/admin/triggerExport")
  public void triggerManualExport() {
    exportJobService.collectStatisticsForPastWeek();
  }

}
