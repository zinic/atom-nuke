package org.atomnuke.service.jetty.server;

import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author zinic
 */
public interface ContextBuilder {

   ServletContextHandler newContext(String contextPath);
}
