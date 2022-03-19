package be.mathiasbosman.inverterdataexport;

import be.mathiasbosman.inverterdataexport.domain.PvStatistics;
import java.time.LocalDate;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PvStatisticStub implements PvStatistics {

  private LocalDate date;
  private double pvTotal;

  public PvStatisticStub() {
    this.date = LocalDate.now();
    Random random = new Random();
    this.pvTotal = random.nextDouble();
  }
}
