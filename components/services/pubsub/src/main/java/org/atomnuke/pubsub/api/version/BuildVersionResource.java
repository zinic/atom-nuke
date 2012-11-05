package org.atomnuke.pubsub.api.version;

import org.atomnuke.pubsub.api.ApiResource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author zinic
 */
@Path("/build_info")
public class BuildVersionResource extends ApiResource {

   @GET
   @Produces("text/plain")
   public Response buildInfo() {
      return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("0.1").build();
   }
}
