package org.atomnuke.task.polling;

/**
 *
 * @author zinic
 */
public interface TaskFuture {

   void onTaskCompletion(Runnable runnable);
}
