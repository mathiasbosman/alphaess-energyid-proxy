package be.mathiasbosman.alphaessenergyidproxy.controller;

import be.mathiasbosman.alphaessenergyidproxy.domain.energyid.ExportJobService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProxyController {

  private final ExportJobService exportJobService;

  @GetMapping("/rest/admin/triggerExport")
  public void triggerManualExport() {
    LocalDate startDate = LocalDate.of(2021, 10, 17);
    LocalDate endDate = LocalDate.now().minusDays(1);
    exportJobService.collectStatisticsForPeriod(startDate, endDate, 100);
  }

}
