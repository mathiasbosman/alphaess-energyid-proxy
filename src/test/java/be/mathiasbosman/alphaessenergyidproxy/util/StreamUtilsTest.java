package be.mathiasbosman.alphaessenergyidproxy.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;

class StreamUtilsTest {

  @Test
  void createBatch() {
    List<String> input = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    Collection<List<String>> batch = StreamUtils.createBatch(input, 2);
    assertThat(batch).hasSize(5);
  }

  @Test
  void createBatchWithZeroLength() {
    List<String> input = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    Collection<List<String>> batch = StreamUtils.createBatch(input, 0);
    assertThat(batch).hasSize(1);
  }
}
