package org.atomnuke.pubsub.api;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.atomnuke.pubsub.sub.SubscriptionManager;

/**
 *
 * @author zinic
 */
@Path("/")
public class ApiResource {

   @Context
   private ServletContext servletContext;

   public void setServletContext(ServletContext servletContext) {
      this.servletContext = servletContext;
   }

   public SubscriptionManager subscriptionManager() {
      return (SubscriptionManager) servletContext.getAttribute(SubscriptionManager.SERVLET_CTX_NAME);
   }
}
