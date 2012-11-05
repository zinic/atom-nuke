package org.atomnuke.pubsub.api.pubsub;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.pubsub.api.ApiResource;
import org.atomnuke.pubsub.api.type.SubscriptionDocument;
import org.atomnuke.pubsub.service.config.SubscriptionException;

/**
 *
 * @author zinic
 */
@Path("/subscriptions")
public class SubscriptionsResource extends ApiResource {

   @PUT
   @Consumes({"application/json", "text/json"})
   public Response createNewSubscription(SubscriptionDocument doc) {
      if (StringUtils.isEmpty(doc.getId()) || StringUtils.isEmpty(doc.getCallback()) || doc.getCategories().isEmpty()) {
         return Response.status(Response.Status.BAD_REQUEST).entity("Please validate your JSON input.").build();
      }

      try {
         subscriptionManager().put(doc);
      } catch (SubscriptionException se) {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(se.getMessage()).build();
      }

      return Response.status(Response.Status.ACCEPTED).build();
   }

   @GET
   @Produces({"application/json", "text/json"})
   public Response getActiveSubscriptions(@PathParam("subscriptionId") String subscriptionId) {
      return Response.ok(subscriptionManager().getAll()).build();
   }

   @GET
   @Path("/{subscriptionId}")
   @Produces({"application/json", "text/json"})
   public Response getActiveSubscription(@PathParam("subscriptionId") String subscriptionId) {
      final SubscriptionDocument doc = subscriptionManager().get(subscriptionId);

      if (doc == null) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }

      return Response.ok(doc).build();
   }
}
