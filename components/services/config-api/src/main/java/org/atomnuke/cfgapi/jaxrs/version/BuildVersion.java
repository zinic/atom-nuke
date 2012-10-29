package org.atomnuke.cfgapi.jaxrs.version;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author zinic
 */
@Path("/_build_info")
public class BuildVersion {

   @GET
   public Response buildInfo() {
      return Response.status(Response.Status.OK).entity("0.1").build();
   }
}
