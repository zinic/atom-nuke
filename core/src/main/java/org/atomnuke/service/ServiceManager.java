package org.atomnuke.service;

import java.util.Collection;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public interface ServiceManager extends Reclaimable {

   void submit(InstanceContext<Service> service) throws ServiceAlreadyRegisteredException;

   void resolve();

   boolean serviceRegistered(String serviceName);

   boolean serviceRegistered(Class serviceInterface);

   Collection<String> servicesAdvertising(Class serviceInterface);

   <T> T get(String serviceName, Class<T> serviceInterface);
}
