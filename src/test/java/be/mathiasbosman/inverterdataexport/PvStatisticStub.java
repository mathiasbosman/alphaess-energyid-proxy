package be.mathiasbosman.inverterdataexport;

import be.mathiasbosman.inverterdataexport.domain.PvStatistics;
import java.time.LocalDate;
import java.util.Random;

public class PvStatisticStub implements PvStatistics {

  @Override
  public LocalDate getDate() {
    return LocalDate.now();
  }

  @Override
  public double getPvTotal() {
    Random random = new Random();
    return random.nextDouble();
  }
}
