package org.atomnuke.service.lifecycle;

import org.atomnuke.service.ServiceInitializationException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.util.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public interface ServiceLifeCycle extends Reclaimable {

   void init(ServiceContext sc) throws ServiceInitializationException;
}
