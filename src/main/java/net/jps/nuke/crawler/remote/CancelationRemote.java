package net.jps.nuke.crawler.remote;

/**
 *
 * @author zinic
 */
public interface CancelationRemote {

   void cancel();

   boolean canceled();
}
