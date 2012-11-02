package org.atomnuke.pubsub.sub;

import org.atomnuke.control.api.type.SubscriptionDocument;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ActiveSubscription {

   private final SubscriptionSelector subscriptionSelector;
   private final SubscriptionEventlet subscriptionEventlet;
   private final CancellationRemote cancellationRemote;
   private SubscriptionDocument subscriptionDocument;

   public ActiveSubscription(SubscriptionSelector subscriptionSelector, SubscriptionEventlet subscriptionEventlet, CancellationRemote cancellationRemote) {
      this.subscriptionSelector = subscriptionSelector;
      this.subscriptionEventlet = subscriptionEventlet;
      this.cancellationRemote = cancellationRemote;
   }

   public void cancel() {
      cancellationRemote.cancel();
   }

   public SubscriptionDocument subscriptionDocument() {
      return subscriptionDocument;
   }

   public SubscriptionSelector subscriptionSelector() {
      return subscriptionSelector;
   }

   public SubscriptionEventlet subscriptionEventlet() {
      return subscriptionEventlet;
   }
}
