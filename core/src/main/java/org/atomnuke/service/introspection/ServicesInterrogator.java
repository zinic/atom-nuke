package org.atomnuke.service.introspection;

import org.atomnuke.service.ServiceUnavailableException;

/**
 *
 * @author zinic
 */
public interface ServicesInterrogator {

   <T> T firstAvailable(Class<T> serviceClass) throws ServiceUnavailableException;
}
