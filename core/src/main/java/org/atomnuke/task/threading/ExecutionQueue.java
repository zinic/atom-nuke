package org.atomnuke.task.threading;

import java.util.concurrent.Future;

/**
 *
 * @author zinic
 */
public interface ExecutionQueue {

   void destroy();

   Future submit(Runnable r);

   <T> Future<T> submit(Runnable r, T stateObject);

   boolean isFull();
}
