package be.mathiasbosman.alphaessenergyidproxy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractAppTest {

  @Test
  void contextLoads() {
  }

}
