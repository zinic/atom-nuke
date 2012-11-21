package org.atomnuke.service;

import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.plugin.InstanceContext;

/**
 *
 * @author zinic
 */
public interface ResolutionHandler {

   ResolutionAction resolve(InstanceContext<? extends Service> serviceInstance);
}
