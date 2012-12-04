package org.atomnuke.pubsub.sub;

import org.atomnuke.pubsub.eventlet.SubscriptionEventlet;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.atomnuke.pubsub.api.type.SubscriptionCategory;
import org.atomnuke.pubsub.api.type.SubscriptionDocument;
import org.atomnuke.pubsub.api.type.SubscriptionDocumentCollection;
import org.atomnuke.pubsub.service.config.SubscriptionException;
import org.atomnuke.sink.eps.AtomEventletHandler;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class TemporarySubscriptionManager implements SubscriptionManager {

   private final Map<String, ActiveSubscription> activeSubscriptions;
   private final AtomEventletHandler eventletHandler;
   private final HttpClient httpClient;

   public TemporarySubscriptionManager(HttpClient httpClient, AtomEventletHandler eventletHandler) {
      this.httpClient = httpClient;
      this.eventletHandler = eventletHandler;

      activeSubscriptions = new HashMap<String, ActiveSubscription>();
   }

   @Override
   public synchronized boolean has(SubscriptionDocument searchDoc) {
      return false;
   }

   @Override
   public synchronized void put(SubscriptionDocument sdoc) throws SubscriptionException {
      // Get the eventlet configuration reference
      ActiveSubscription subscription = activeSubscriptions.get(sdoc.getId());

      // Make sure we have categories to work with
      if (subscription == null && !sdoc.getCategories().isEmpty()) {
         final SubscriptionEventlet eventlet = new SubscriptionEventlet(httpClient, sdoc.getId(), sdoc.getCallback());
         final RegexCategorySelector selector = new RegexCategorySelector();
         final CancellationRemote cancellationRemote = eventletHandler.enlistHandler(eventlet, selector);

         subscription = new ActiveSubscription(selector, eventlet, cancellationRemote);
         activeSubscriptions.put(sdoc.getCallback(), subscription);

         // Subscribe
         for (SubscriptionCategory subscriptionCategory : sdoc.getCategories()) {
            subscription.subscriptionSelector().selectOn(subscriptionCategory);
         }
      }
   }

   @Override
   public synchronized SubscriptionDocument get(String callback) {
      return new SubscriptionDocument();
   }

   @Override
   public synchronized SubscriptionDocumentCollection getAll() {
      final SubscriptionDocumentCollection subCollection = new SubscriptionDocumentCollection();

      return subCollection;
   }
}
