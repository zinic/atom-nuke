package net.jps.nuke.listener.eps.handler;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.service.Service;

/**
 *
 * @author zinic
 */
public interface AtomEventlet extends Service {

   void entry(Entry entry) throws AtomEventletException;
}
