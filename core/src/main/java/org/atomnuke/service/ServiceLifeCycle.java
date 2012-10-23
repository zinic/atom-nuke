package org.atomnuke.service;

import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.lifecycle.ResourceLifeCycle;

/**
 *
 * @author zinic
 */
public interface ServiceLifeCycle extends ResourceLifeCycle<ServiceContext> {

   ResolutionAction resolve(ServiceManager serviceManager);
}
