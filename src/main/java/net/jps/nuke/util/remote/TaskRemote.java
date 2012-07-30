package net.jps.nuke.util.remote;

/**
 *
 * @author zinic
 */
public interface TaskRemote {

   Runnable nextTask();

   void updateTask(Runnable task);
}
