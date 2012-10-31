package org.atomnuke.pubsub;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.Parameter;
import org.atomnuke.config.model.Parameters;
import org.atomnuke.control.api.type.SubscriptionDocument;
import org.atomnuke.control.api.type.SubscriptionDocumentCollection;
import org.atomnuke.control.service.config.ServerCfgUpdateManager;
import org.atomnuke.control.service.config.SubscriptionException;
import org.atomnuke.fallout.config.server.ServerConfigurationHandler;

/**
 *
 * @author zinic
 */
public class SubscriptionManagerImpl implements SubscriptionManager {

   private static final String CALLBACK_PROPERTY = "org.atomnuke.pubsub.publicsher.CallBackUrls";
   private static final String SUBSCRIBER_PROPERTY = "org.atomnuke.pubsub.subscriber.Id";
   private static final String EXPECTED_RELAY_NAME = "pubsub-relay";

   private final Map<String, SubscriptionDocument> activeSubscriptions;
   private final ServerConfigurationHandler configurationHandler;
   private final ServerCfgUpdateManager servercfgUpdateManager;

   public SubscriptionManagerImpl(ServerConfigurationHandler configurationHandler, ServerCfgUpdateManager servercfgUpdateManager) {
      this.configurationHandler = configurationHandler;
      this.servercfgUpdateManager = servercfgUpdateManager;

      activeSubscriptions = new HashMap<String, SubscriptionDocument>();
   }

   @Override
   public synchronized void put(SubscriptionDocument sdoc, String callbackUrl) throws SubscriptionException {
      // Check the relay
      if (configurationHandler.findRelay(EXPECTED_RELAY_NAME) == null) {
         throw new SubscriptionException("Unable to locate expected relay. The configuration most likely has been modified outside of this controller's context.");
      }

      // Get the eventlet configuration reference
      final Eventlet eventlet = getSubscriptionEventlet(sdoc, callbackUrl);

      // Bind it
      configurationHandler.bind(EXPECTED_RELAY_NAME, eventlet.getId());

      // Update our listeners
      servercfgUpdateManager.write(configurationHandler.getConfiguration());
   }

   private Eventlet getSubscriptionEventlet(SubscriptionDocument sdoc, String callbackUrl) {
      // Subscriber ID parameter
      final Parameter subscriberParameter = new Parameter();
      subscriberParameter.setName(SUBSCRIBER_PROPERTY);
      subscriberParameter.setValue(sdoc.getId());

      final Eventlet foundEventlet = configurationHandler.firstEventletByParameter(subscriberParameter);

      return foundEventlet != null ? foundEventlet : addSubscriptionEventlet(sdoc, callbackUrl, subscriberParameter);
   }

   private Eventlet addSubscriptionEventlet(SubscriptionDocument sdoc, String callbackUrl, Parameter subscriberId) {
      final Eventlet eventlet = new Eventlet();
      eventlet.setId(UUID.randomUUID().toString());
      eventlet.setHref(SubscriptionEventlet.class.getName());
      eventlet.setType(LanguageType.JAVA);

      // Callback URL parameter
      final Parameter callbackParameter = new Parameter();
      callbackParameter.setName(CALLBACK_PROPERTY);
      callbackParameter.setValue(callbackUrl);

      // Add the event params
      final Parameters eventletParameters = new Parameters();
      eventletParameters.getParam().add(callbackParameter);
      eventletParameters.getParam().add(subscriberId);

      eventlet.setParameters(eventletParameters);

      configurationHandler.addEventlet(eventlet);

      return eventlet;
   }

   @Override
   public synchronized SubscriptionDocument get(String id) {
      return activeSubscriptions.get(id);
   }

   @Override
   public synchronized SubscriptionDocumentCollection getAll() {
      final SubscriptionDocumentCollection subCollection = new SubscriptionDocumentCollection();
      subCollection.addAll(activeSubscriptions.values());

      return subCollection;
   }
}
