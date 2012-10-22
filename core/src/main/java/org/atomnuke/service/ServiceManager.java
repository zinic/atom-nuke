package org.atomnuke.service;

import java.util.Collection;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public interface ServiceManager extends Reclaimable {

   void register(InstanceContext<Service> service) throws ServiceAlreadyRegisteredException;

   boolean nameRegistered(String serviceName);

   boolean serviceRegistered(Class serviceInterface);

   Collection<String> listRegisteredServicesFor(Class serviceInterface);

   <T> T get(String serviceName, Class<T> serviceInterface);
}
