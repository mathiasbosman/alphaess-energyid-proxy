package be.mathiasbosman.inverterdataexport.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

/**
 * Utils for streams.
 */
@UtilityClass
public class CollectionUtils {

  /**
   * Splits input list in batches. If size of batch is null or zero the whole list will be
   * returned.
   *
   * @param inputList The full list
   * @param batchSize Amount of items per batch
   * @param <T>       Type of list objects
   * @return Collections of batches
   */
  public static <T> Collection<List<T>> split(Collection<T> inputList, Integer batchSize) {
    AtomicInteger counter = new AtomicInteger();
    int groupSize = batchSize != null && batchSize > 0 ? batchSize : inputList.size();
    return inputList.stream()
        .collect(Collectors.groupingBy(gr -> counter.getAndIncrement() / groupSize))
        .values();
  }
}
