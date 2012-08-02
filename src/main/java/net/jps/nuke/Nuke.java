package net.jps.nuke;

import net.jps.nuke.task.Tasker;

/**
 *
 * @author zinic
 */
public interface Nuke {

   void start();

   void destroy();

   Tasker tasker();
}
