package org.atomnuke.auth.service.rackspace;

import com.rackspace.docs.identity.api.ext.rax_kskey.v1.ApiKeyCredentials;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.auth.AuthServiceException;
import org.atomnuke.rackspace.auth.v2.RackspaceAuthClient;
import org.atomnuke.source.crawler.auth.AuthenticationHandler;
import org.openstack.docs.identity.api.v2.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class RackspaceAuthenticationHandler implements AuthenticationHandler {

   private static final Logger LOG = LoggerFactory.getLogger(RackspaceAuthenticationHandler.class);

   private final RackspaceAuthClient authClient;
   private final ApiKeyCredentials credentials;
   private final Map<String, String> headers;

   public RackspaceAuthenticationHandler(RackspaceAuthClient authClient, String username, String apiKey) {
      this.authClient = authClient;
      this.headers = new HashMap<String, String>();
      
      credentials = new ApiKeyCredentials();
      credentials.setUsername(username);
      credentials.setApiKey(apiKey);
   }

   @Override
   public synchronized Map<String, String> authenticationHeaders() {
      if (!headers.isEmpty()) {
         return Collections.unmodifiableMap(headers);
      }
      
      return Collections.EMPTY_MAP;
   }

   @Override
   public synchronized void authenticate() {
      try {
         final Token token = authClient.authenticate(credentials);
         
         headers.clear();
         headers.put("X-Auth-Token", token.getId());
      } catch (AuthServiceException ase) {
         LOG.error("Failed to perform authentication against Rackspace auth. Error: " + ase.getMessage(), ase);
      }
   }
}
