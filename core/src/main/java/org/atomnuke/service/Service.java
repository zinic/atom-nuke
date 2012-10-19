package org.atomnuke.service;

import org.atomnuke.service.lifecycle.ServiceLifeCycle;

/**
 *
 * @author zinic
 */
public interface Service extends ServiceLifeCycle {

   String name();

   boolean provides(Class serviceInterface);

   Object instance();
}
