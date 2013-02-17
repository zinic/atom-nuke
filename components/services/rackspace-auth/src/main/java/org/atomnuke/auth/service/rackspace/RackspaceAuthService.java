package org.atomnuke.auth.service.rackspace;

import java.io.File;
import java.net.URI;
import java.util.Properties;
import org.apache.http.client.HttpClient;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.container.service.annotation.Requires;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.rackspace.auth.v2.RackspaceAuthClient;
import org.atomnuke.rackspace.auth.v2.RackspaceAuthClientImpl;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.io.file.FileConfigurationManager;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;
import org.atomnuke.util.config.io.marshall.properties.PropertiesConfigMarshaller;

/**
 *
 * @author zinic
 */
@NukeService
@Requires({HttpClient.class})
public class RackspaceAuthService extends AbstractRuntimeService {

   private static final String PROPERTIES_FILE_LOCATION_PROPERTY = "rackspace.auth.properties.location",
           DEFAULT_PROPERTIES_FILE_LOCATION = "/etc/atomnuke/rackspace_auth.properties",
           AUTH_URI_PROPERTY = "rackspace.auth.uri",
           AUTH_USERNAME_PROPERTY = "rackspace.auth.username",
           AUTH_APIKEY_PROPERTY = "rackspace.auth.apikey";

   private RackspaceAuthenticationHandler serviceImpl;

   public RackspaceAuthService() {
      super(RackspaceAuthenticationHandler.class);
   }

   @Override
   public void init(ServiceContext context) throws InitializationException {
      String propertiesLocation = DEFAULT_PROPERTIES_FILE_LOCATION;
      
      // Check to see if a global parameter overwrites where we should load our configuration from
      if (context.parameters().containsKey(PROPERTIES_FILE_LOCATION_PROPERTY)) {
         propertiesLocation = context.parameters().get(PROPERTIES_FILE_LOCATION_PROPERTY);
      }
      
      final File configurationFile = new File(propertiesLocation);
      
      // Build the marshaller and manager for our configuration
      final ConfigurationMarshaller<Properties> cfgMarshaller = new PropertiesConfigMarshaller();
      final ConfigurationManager<Properties> cfgManager = new FileConfigurationManager<Properties>(cfgMarshaller, configurationFile);

      try {
         // Get an HttpClient to use
         final HttpClient httpClient = context.services().firstAvailable(HttpClient.class);
         
         // Read our configuration
         final Properties cfgProperties = cfgManager.read();
         checkConfig(propertiesLocation, cfgProperties);
         
         final URI authUri = URI.create(cfgProperties.getProperty(AUTH_URI_PROPERTY));
         final RackspaceAuthClient authClient = new RackspaceAuthClientImpl(httpClient, authUri);
         
         serviceImpl = new RackspaceAuthenticationHandler(authClient,
                 cfgProperties.getProperty(AUTH_USERNAME_PROPERTY),
                 cfgProperties.getProperty(AUTH_APIKEY_PROPERTY));
      } catch (Exception sue) {
         throw new InitializationException(sue);
      }
   }

   private void checkConfig(String location, Properties props) throws InitializationException {
      if (!props.containsKey(AUTH_URI_PROPERTY)) {
         throw new InitializationException("Property: " + AUTH_URI_PROPERTY + " required in properties file: " + location);
      }
      
      if (!props.containsKey(AUTH_USERNAME_PROPERTY)) {
         throw new InitializationException("Property: " + AUTH_USERNAME_PROPERTY + " required in properties file: " + location);
      }
      
      if (!props.containsKey(AUTH_APIKEY_PROPERTY)) {
         throw new InitializationException("Property: " + AUTH_APIKEY_PROPERTY + " required in properties file: " + location);
      }
   }

   @Override
   public Object instance() {
      return serviceImpl;
   }
}
