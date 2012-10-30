package org.atomnuke.control.pubsub;

import java.util.HashMap;
import java.util.Map;
import org.atomnuke.control.api.type.SubscriptionDocument;
import org.atomnuke.control.service.config.ConfigurationModelControllerImpl;

/**
 *
 * @author zinic
 */
public class SubscriptionManagerImpl implements SubscriptionManager {

   private final Map<String, SubscriptionDocument> activeSubscriptions;
   private final ConfigurationModelControllerImpl modelController;

   public SubscriptionManagerImpl(ConfigurationModelControllerImpl modelController) {
      this.modelController = modelController;

      activeSubscriptions = new HashMap<String, SubscriptionDocument>();
   }

   @Override
   public synchronized void put(SubscriptionDocument doc) {
      activeSubscriptions.put(doc.getId(), doc);
   }

   @Override
   public synchronized SubscriptionDocument get(String id) {
      return activeSubscriptions.get(id);
   }
}
