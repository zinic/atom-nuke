package org.atomnuke.nuke.util.remote;

/**
 * A cancellation remote is a thread safe object that can be utilized to
 * communicate when a running operation should cancel itself.
 *
 * @author zinic
 */
public interface CancellationRemote {

   /**
    * Sets a signal that the operation related to this remote should be canceled
    * as soon as possible.
    */
   void cancel();

   /**
    * Returns whether or not cancellation has been called for.
    * 
    * @return 
    */
   boolean canceled();
}
