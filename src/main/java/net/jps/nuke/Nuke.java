package net.jps.nuke;

import net.jps.nuke.task.TaskSubmitter;

/**
 *
 * @author zinic
 */
public interface Nuke extends TaskSubmitter {

   void start();

   void destroy();
}
