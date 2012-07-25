package net.jps.nuke.crawler.threading;

import net.jps.nuke.crawler.task.ManagedTask;
import com.rackspace.papi.commons.util.Destroyable;

/**
 *
 * @author zinic
 */
public interface ExecutionManager extends Destroyable {

   void destroy();

   void submit(ManagedTask task);

   boolean submitted(ManagedTask task);
}
