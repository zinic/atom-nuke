package net.jps.nuke.util.remote;

/**
 *
 * @author zinic
 */
public interface CancellationRemote {

   void cancel();

   boolean canceled();
}
