package org.atomnuke.control.api.pubsub;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.control.api.ApiResource;
import org.atomnuke.control.api.type.SubscriptionDocument;
import org.atomnuke.control.service.config.SubscriptionException;

/**
 *
 * @author zinic
 */
@Path("/subscriptions")
public class SubscriptionsResource extends ApiResource {

   @GET
   @Path("/active")
   @Produces({"application/json", "text/json"})
   public Response getActiveSubscriptions(@PathParam("subscriptionId") String subscriptionId) {
      return Response.ok(subscriptionManager().getAll()).build();
   }

   @GET
   @Path("/active/{subscriptionId}")
   @Produces({"application/json", "text/json"})
   public Response getActiveSubscription(@PathParam("subscriptionId") String subscriptionId) {
      final SubscriptionDocument doc = subscriptionManager().get(subscriptionId);

      if (doc == null) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }

      return Response.ok(doc).build();
   }

   @PUT
   @Path("/new")
   @Consumes({"application/json", "text/json"})
   public Response createNewSubscription(@HeaderParam("X-CALLBACK") String callbackUri, SubscriptionDocument doc) {
      if (StringUtils.isEmpty(doc.getId()) || doc.getCategories().isEmpty()) {
         return Response.status(Response.Status.BAD_REQUEST).build();
      }

      try {
         subscriptionManager().put(doc, callbackUri);
      } catch (SubscriptionException se) {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(se.getMessage()).build();
      }

      return Response.status(Response.Status.ACCEPTED).build();
   }
}
