package be.mathiasbosman.inverterdataexport.controller;

import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * Extra error attributes for the exception handling.
 */
@Slf4j
@Component
public class ErrorAttributesWithUuid extends DefaultErrorAttributes {

  public static final String ERROR_ID = "errorId";
  public static final String THROWABLE = "throwable";
  public static final String WEB_REQUEST = "webRequest";

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest,
      ErrorAttributeOptions options) {
    Throwable throwable = getError(webRequest);
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
    errorAttributes.put(THROWABLE, throwable);
    errorAttributes.put(WEB_REQUEST, webRequest.toString());
    errorAttributes.put(ERROR_ID,
        errorAttributes.computeIfAbsent(ERROR_ID, k -> UUID.randomUUID()));
    return errorAttributes;
  }
}
