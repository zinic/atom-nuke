package org.atomnuke.service.introspection;

import org.atomnuke.service.ServiceUnavailableException;

/**
 *
 * @author zinic
 */
public interface ServicesInterrogator {

   /**
    * Finds a registered service by its service interface definition as well as
    * its name. If there is no service with the required interface and name
    * registered then this method will throw an exception.
    * 
    * @param <T>
    * @param serviceClass
    * @return
    * @throws ServiceUnavailableException 
    */
   <T> T lookup(String serviceName, Class<T> serviceClass) throws ServiceUnavailableException;
   
   /**
    * Finds the first registered service that advertises this service
    * interface definition. If there is no service with the required interface 
    * class registered then this method will throw an exception.
    * 
    * @param <T>
    * @param serviceClass
    * @return
    * @throws ServiceUnavailableException 
    */
   <T> T firstAvailable(Class<T> serviceClass) throws ServiceUnavailableException;
}
