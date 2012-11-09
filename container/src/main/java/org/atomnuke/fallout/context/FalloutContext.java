package org.atomnuke.fallout.context;

import org.atomnuke.fallout.config.server.ServerConfigurationHandler;
import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.config.ConfigurationException;

/**
 *
 * @author zinic
 */
public interface FalloutContext {

   void enlistActor(String name, InstanceContext<? extends Reclaimable> actor);

   void process(ServerConfigurationHandler cfgHandler) throws ConfigurationException;
}
