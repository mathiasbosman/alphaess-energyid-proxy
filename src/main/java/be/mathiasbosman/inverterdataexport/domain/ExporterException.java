package be.mathiasbosman.inverterdataexport.domain;

import org.slf4j.event.Level;

/**
 * Exception used for validations.
 */
public class ExporterException extends RuntimeException {

  private final Level logLevel;

  public ExporterException(Level logLevel, String message, Object... args) {
    super(String.format(message, args));
    this.logLevel = logLevel;
  }

  public Level getLogLevel() {
    return logLevel;
  }
}
