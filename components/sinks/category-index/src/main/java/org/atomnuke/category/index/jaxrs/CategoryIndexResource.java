package org.atomnuke.category.index.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author zinic
 */
@Path("/")
public class CategoryIndexResource {

   public static final String CAT_TRACKER_CTX_NAME = "org.atomnuke.category.index.CategoryTracker";

   @GET
   @Produces({"application/json"})
   public Response getCategoryIndicies() {

      return Response.ok().build();
   }
}
