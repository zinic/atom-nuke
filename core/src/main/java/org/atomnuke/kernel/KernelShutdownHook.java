package org.atomnuke.kernel;

/**
 *
 * @author zinic
 */
public interface KernelShutdownHook {

   void enlistShutdownHook(Runnable r);

   void shutdown();
}
