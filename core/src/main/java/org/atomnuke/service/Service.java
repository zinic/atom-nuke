package org.atomnuke.service;

import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public interface Service extends ResourceLifeCycle<ServiceContext> {

   String name();

   boolean provides(Class serviceInterface);

   Object instance();
}
