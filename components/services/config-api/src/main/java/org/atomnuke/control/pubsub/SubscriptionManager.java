package org.atomnuke.control.pubsub;

import org.atomnuke.control.api.type.SubscriptionDocument;

/**
 *
 * @author zinic
 */
public interface SubscriptionManager {

   static final String SERVLET_CTX_NAME = "org.atomnuke.control.pubsub.SubscriptionManager";

   SubscriptionDocument get(String id);

   void put(SubscriptionDocument doc);
}
