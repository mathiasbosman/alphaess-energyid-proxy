package be.mathiasbosman.inverterdataexport.collector.alphaess.dto;

import be.mathiasbosman.inverterdataexport.collector.alphaess.AlphaessProperties.Credentials;

/**
 * Dto to post to the authentication endpoint.
 */
public record LoginDto(String username, String password) {

  public static LoginDto fromCredentials(Credentials credentials) {
    return new LoginDto(credentials.getUsername(), credentials.getPassword());
  }
}
