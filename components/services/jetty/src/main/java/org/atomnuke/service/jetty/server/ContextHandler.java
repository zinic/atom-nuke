package org.atomnuke.service.jetty.server;

import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author zinic
 */
public interface ContextHandler {

   void addServlet(ServletHolder holder, String pathSpec);
}
