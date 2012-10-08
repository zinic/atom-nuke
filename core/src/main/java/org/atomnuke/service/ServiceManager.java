package org.atomnuke.service;

import org.atomnuke.kernel.resource.Destroyable;
import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public interface ServiceManager extends Destroyable {

   void register(InstanceContext<Service> service);

   <T> T findService(Class<T> serviceInterface);
}
