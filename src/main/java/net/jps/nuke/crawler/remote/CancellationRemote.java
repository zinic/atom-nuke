package net.jps.nuke.crawler.remote;

/**
 *
 * @author zinic
 */
public interface CancellationRemote {

   void cancel();

   boolean canceled();
}
