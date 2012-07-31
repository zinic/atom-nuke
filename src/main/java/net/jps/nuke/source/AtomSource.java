package net.jps.nuke.source;

import net.jps.nuke.service.Service;

/**
 *
 * @author zinic
 */
public interface AtomSource extends Service {

   AtomSourceResult poll() throws AtomSourceException;
}
