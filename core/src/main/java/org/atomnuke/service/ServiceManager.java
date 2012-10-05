package org.atomnuke.service;

import org.atomnuke.kernel.resource.Destroyable;

/**
 *
 * @author zinic
 */
public interface ServiceManager extends Destroyable {

   void register(Service service);

   <T> T findService(Class<T> serviceInterface);
}
