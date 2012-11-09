package org.atomnuke.service;

import org.atomnuke.lifecycle.ResourceLifeCycle;
import org.atomnuke.lifecycle.resolution.ResolutionAction;

/**
 *
 * @author zinic
 */
public interface ServiceLifeCycle extends ResourceLifeCycle<ServiceContext> {

   ResolutionAction resolve(ServiceManager serviceManager);
}
