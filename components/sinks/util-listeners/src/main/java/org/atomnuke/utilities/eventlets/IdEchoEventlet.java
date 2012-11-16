package org.atomnuke.utilities.eventlets;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.sink.eps.eventlet.AtomEventletPartial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class IdEchoEventlet extends AtomEventletPartial {

   private static final Logger LOG = LoggerFactory.getLogger(IdEchoEventlet.class);

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      if (entry.id() != null) {
         LOG.info(entry.id().toString());
      }
   }
}
