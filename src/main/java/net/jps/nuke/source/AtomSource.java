package net.jps.nuke.source;

/**
 *
 * @author zinic
 */
public interface AtomSource {

   AtomSourceResult poll() throws AtomSourceException;
}
