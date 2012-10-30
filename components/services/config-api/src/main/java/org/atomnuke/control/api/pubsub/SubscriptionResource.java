package org.atomnuke.control.api.pubsub;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.atomnuke.control.api.ApiResource;

/**
 *
 * @author zinic
 */
@Path("/subscrption")
public class SubscriptionResource extends ApiResource {

   @GET
   @Path("/active/{subscriptionId}")
   public Response getActiveSubscription(@PathParam("subscriptionId") String subscriptionId) {
      return Response.ok().build();
   }

   @POST
   @Path("/new")
   public Response createNewSubscription(@HeaderParam("CALLBACK-URI") String subscriptionId) {
      return Response.ok().build();
   }
}
