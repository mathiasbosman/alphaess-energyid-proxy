package be.mathiasbosman.alphaessexport.domain.dto;

import be.mathiasbosman.alphaessexport.config.AlphaessProperties.Credentials;

/**
 * Dto to post to the authentication endpoint.
 */
public record LoginDto(String username, String password) {

  public static LoginDto fromCredentials(Credentials creds) {
    return new LoginDto(creds.getUsername(), creds.getPassword());
  }
}
