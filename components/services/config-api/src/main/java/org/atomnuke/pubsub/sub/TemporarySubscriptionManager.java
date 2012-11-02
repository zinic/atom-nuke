package org.atomnuke.pubsub.sub;

import java.util.HashMap;
import java.util.Map;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.control.api.type.SubscriptionCategory;
import org.atomnuke.control.api.type.SubscriptionDocument;
import org.atomnuke.control.api.type.SubscriptionDocumentCollection;
import org.atomnuke.control.service.config.SubscriptionException;
import org.atomnuke.sink.eps.AtomEventletHandler;

/**
 *
 * @author zinic
 */
public class TemporarySubscriptionManager implements SubscriptionManager {

   private final Map<String, ActiveSubscription> activeSubscriptions;
   private final AtomEventletHandler eventletHandler;

   public TemporarySubscriptionManager(AtomEventletHandler eventletHandler) {
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
      ActiveSubscription subscription = activeSubscriptions.get(sdoc.getCallback());

      if (subscription == null) {
         final SubscriptionEventlet eventlet = new SubscriptionEventlet();
         final SubscriptionSelector selector = new SubscriptionSelector();

         subscription = new ActiveSubscription(new SubscriptionSelector(), new SubscriptionEventlet(), eventletHandler.enlistHandler(eventlet, selector));
         activeSubscriptions.put(sdoc.getCallback(), subscription);
      }

      // Subscribe
      for (SubscriptionCategory subscriptionCategory : sdoc.getCategories()) {
         subscription.subscriptionSelector().addCategory(
                 new CategoryBuilder().setScheme(subscriptionCategory.getScheme()).setTerm(subscriptionCategory.getTerm()).build());
      }
   }

   @Override
   public synchronized SubscriptionDocument get(String callback) {
//      return activeSubscriptions.get(id);
      return new SubscriptionDocument();
   }

   @Override
   public synchronized SubscriptionDocumentCollection getAll() {
      final SubscriptionDocumentCollection subCollection = new SubscriptionDocumentCollection();
//      subCollection.addAll(activeSubscriptions.values());

      return subCollection;
   }
}
