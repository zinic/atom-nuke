package org.atomnuke.service;

import org.atomnuke.plugin.InstanceEnvironment;

/**
 *
 * @author zinic
 */
public interface Service {

   String name();

   InstanceEnvironment<ServiceLifeCycle> instanceEnvironment();
}
