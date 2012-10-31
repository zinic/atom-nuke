package org.atomnuke.pubsub;

import org.atomnuke.control.api.type.SubscriptionDocument;
import org.atomnuke.control.api.type.SubscriptionDocumentCollection;
import org.atomnuke.control.service.config.SubscriptionException;

/**
 *
 * @author zinic
 */
public interface SubscriptionManager {

   static final String SERVLET_CTX_NAME = "org.atomnuke.control.pubsub.SubscriptionManager";

   SubscriptionDocument get(String id);

   SubscriptionDocumentCollection getAll();

   void put(SubscriptionDocument doc, String callback) throws SubscriptionException;
}
