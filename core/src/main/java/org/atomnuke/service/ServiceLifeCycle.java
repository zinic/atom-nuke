package org.atomnuke.service;

import org.atomnuke.service.context.ServiceContext;

/**
 *
 * @author zinic
 */
public interface ServiceLifeCycle {

   void init(ServiceContext sc) throws ServiceInitializationException;

   void destroy();
}
