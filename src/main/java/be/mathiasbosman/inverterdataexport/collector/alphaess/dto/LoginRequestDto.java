package be.mathiasbosman.inverterdataexport.collector.alphaess.dto;

import be.mathiasbosman.inverterdataexport.collector.alphaess.AlphaessProperties.Credentials;

/**
 * Dto to post to the authentication endpoint.
 */
public record LoginRequestDto(String username, String password) {

  public static LoginRequestDto fromCredentials(Credentials credentials) {
    return new LoginRequestDto(credentials.getUsername(), credentials.getPassword());
  }
}
