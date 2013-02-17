package org.atomnuke.source.crawler.auth;

import java.util.Map;

/**
 *
 * @author zinic
 */
public interface AuthenticationHandler {

   Map<String, String> authenticationHeaders();

   void authenticate();
}
