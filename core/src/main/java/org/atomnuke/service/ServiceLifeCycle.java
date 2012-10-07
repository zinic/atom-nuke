package org.atomnuke.service;

import org.atomnuke.service.context.ServiceContext;

/**
 *
 * @author zinic
 */
public interface ServiceLifeCycle {

   boolean provides(Class serviceInterface);

   Object instance();

   void init(ServiceContext sc);

   void destroy();
}
