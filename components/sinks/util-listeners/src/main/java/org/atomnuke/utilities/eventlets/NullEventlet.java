package org.atomnuke.utilities.eventlets;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.sink.eps.eventlet.AtomEventletPartial;

/**
 *
 * @author zinic
 */
public class NullEventlet extends AtomEventletPartial {

   @Override
   public void entry(Entry entry) throws AtomEventletException {
   }
}
