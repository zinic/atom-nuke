package org.atomnuke.pubsub.sub;

import org.atomnuke.pubsub.eventlet.SubscriptionEventlet;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.atomnuke.atom.model.builder.CategoryBuilder;
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

      if (subscription == null && !sdoc.getCategories().isEmpty()) {
         final SubscriptionCategory subscriptionCategory = sdoc.getCategories().iterator().next();
         final SubscriptionEventlet eventlet = new SubscriptionEventlet(subscriptionCategory, httpClient, sdoc.getId(), sdoc.getCallback());
         final SubscriptionSelector selector = new SubscriptionSelector();
         final CancellationRemote cancellationRemote = eventletHandler.enlistHandler(eventlet, selector);

         subscription = new ActiveSubscription(selector, eventlet, cancellationRemote);
         activeSubscriptions.put(sdoc.getCallback(), subscription);

         subscription.subscriptionSelector().addCategory(
                 new CategoryBuilder().setScheme(subscriptionCategory.getScheme()).setTerm(subscriptionCategory.getTerm()).build());
      }

      // Subscribe
//      for (SubscriptionCategory subscriptionCategory : sdoc.getCategories()) {
//         subscription.subscriptionSelector().addCategory(
//                 new CategoryBuilder().setScheme(subscriptionCategory.getScheme()).setTerm(subscriptionCategory.getTerm()).build());
//      }
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
