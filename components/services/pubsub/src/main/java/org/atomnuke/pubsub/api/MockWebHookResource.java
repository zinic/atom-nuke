package org.atomnuke.pubsub.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.atomnuke.pubsub.api.type.SubscriptionDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@Path("/callback")
public class MockWebHookResource {

   private static final Logger LOG = LoggerFactory.getLogger(MockWebHookResource.class);

   @POST
   @Consumes({"application/json"})
   public void event(SubscriptionDocument subscriptionDocument) {
      LOG.info("Recieved webhook callback -> TS: " + subscriptionDocument.getContent().getTimestamp() + " - VALUE: " + subscriptionDocument.getContent().getValue());
   }
}
