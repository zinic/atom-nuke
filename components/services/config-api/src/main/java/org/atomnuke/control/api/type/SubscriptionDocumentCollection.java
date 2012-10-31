package org.atomnuke.control.api.type;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 *
 * @author zinic
 */
public class SubscriptionDocumentCollection {

   private List<SubscriptionDocument> subscriptions;

   public SubscriptionDocumentCollection() {
      subscriptions = new LinkedList<SubscriptionDocument>();
   }

   public void addAll(Collection<SubscriptionDocument> subscriptionsToAdd) {
      subscriptions.addAll(subscriptionsToAdd);
   }

   public List<SubscriptionDocument> getSubscriptions() {
      return subscriptions;
   }

   public void setSubscriptions(List<SubscriptionDocument> subscriptions) {
      this.subscriptions = subscriptions;
   }
}
