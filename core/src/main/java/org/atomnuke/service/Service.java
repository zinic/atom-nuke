package org.atomnuke.service;

import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public interface Service {

   String name();

   InstanceContext<ServiceLifeCycle> instanceContext();
}
