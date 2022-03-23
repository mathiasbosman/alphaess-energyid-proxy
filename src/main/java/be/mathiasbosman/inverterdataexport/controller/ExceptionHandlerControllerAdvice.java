package be.mathiasbosman.inverterdataexport.controller;

import be.mathiasbosman.inverterdataexport.domain.ExporterException;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller Advice for exception handling.
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private final ErrorAttributes errorAttributes;

  @ExceptionHandler
  public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex,
      WebRequest request) {
    return createResponseEntityAndLogException(request, HttpStatus.INTERNAL_SERVER_ERROR, ex,
        Level.ERROR);
  }


  @ExceptionHandler(ExporterException.class)
  public ResponseEntity<Map<String, Object>> handleVimException(ExporterException ex,
      WebRequest request) {
    return createResponseEntityAndLogException(request, HttpStatus.BAD_REQUEST, ex,
        ex.getLogLevel());
  }

  private ResponseEntity<Map<String, Object>> createResponseEntityAndLogException(
      WebRequest request,
      HttpStatus httpStatus, Throwable exception, Level logLevel) {
    ResponseEntity<Map<String, Object>> response = createResponseEntity(request, httpStatus);
    final String logPl = "Error with ID: {} - {} - {}";
    switch (logLevel) {
      case WARN -> log.warn(logPl, errorIdFromErrorAttributes(response), exception.getMessage(),
          request, exception);
      case TRACE -> log.trace(logPl, errorIdFromErrorAttributes(response), exception.getMessage(),
          request, exception);
      case DEBUG -> log.debug(logPl, errorIdFromErrorAttributes(response), exception.getMessage(),
          request, exception);
      case INFO -> log.info(logPl, errorIdFromErrorAttributes(response), exception.getMessage(),
          request, exception);
      default -> log.error(logPl, errorIdFromErrorAttributes(response), exception.getMessage(),
          request, exception);
    }
    return response;
  }

  private Object errorIdFromErrorAttributes(ResponseEntity<Map<String, Object>> response) {
    return Optional.ofNullable(response.getBody())
        .map(body -> body.get(ErrorAttributesWithUuid.ERROR_ID)).orElse("");
  }

  private ResponseEntity<Map<String, Object>> createResponseEntity(WebRequest request,
      HttpStatus httpStatus) {
    Map<String, Object> attributes = errorAttributes.getErrorAttributes(request,
        ErrorAttributeOptions.of(Include.MESSAGE, Include.EXCEPTION));
    attributes.put("status", httpStatus.value());
    attributes.put("error", httpStatus.getReasonPhrase());
    attributes.remove(ErrorAttributesWithUuid.THROWABLE);
    return ResponseEntity.status(httpStatus).body(attributes);
  }
}
