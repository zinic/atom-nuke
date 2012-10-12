package org.atomnuke.service;

import java.util.Collection;
import org.atomnuke.kernel.resource.Destroyable;
import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public interface ServiceManager extends Destroyable {

   void register(InstanceContext<Service> service) throws ServiceAlreadyRegisteredException;

   boolean isRegistered(String serviceName);

   Collection<String> listRegisteredServicesFor(Class serviceInterface);

   <T> T get(String serviceName, Class<T> serviceInterface);
}
