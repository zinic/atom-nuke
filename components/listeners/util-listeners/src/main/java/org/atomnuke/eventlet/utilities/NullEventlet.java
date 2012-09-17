package org.atomnuke.eventlet.utilities;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.listener.eps.eventlet.AtomEventletPartial;

/**
 *
 * @author zinic
 */
public class NullEventlet extends AtomEventletPartial {

   @Override
   public void entry(Entry entry) throws AtomEventletException {
   }
}
