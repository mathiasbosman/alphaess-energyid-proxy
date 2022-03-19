package be.mathiasbosman.inverterdataexport.controller;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.mathiasbosman.inverterdataexport.AbstractControllerTest;
import be.mathiasbosman.inverterdataexport.domain.ExporterException;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ExceptionHandlerControllerAdviceTest extends AbstractControllerTest {

  @MockBean
  private ExceptionServiceStub exceptionServiceStub;

  @Test
  void testRunTimeException() throws Exception {
    when(exceptionServiceStub.throwException()).thenThrow(new RuntimeException("foo bar"));

    testExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "foo bar");
  }

  @Test
  void testBlogException() throws Exception {
    for (Level logLevel : Level.values()) {
      reset(exceptionServiceStub);
      when(exceptionServiceStub.throwException()).thenThrow(
          new ExporterException(logLevel, logLevel.name()));

      testExceptionResponse(HttpStatus.BAD_REQUEST, logLevel.name());
    }
  }

  private void testExceptionResponse(HttpStatus status, String message)
      throws Exception {
    mvc.perform(get("/exceptionStub/throw"))
        .andExpect(status().is(status.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(status.value()))
        .andExpect(jsonPath("$.message").value(message))
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.errorId").exists())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.exception").exists());
  }
}
