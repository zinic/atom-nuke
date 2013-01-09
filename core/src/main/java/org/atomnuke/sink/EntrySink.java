package org.atomnuke.sink;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public abstract class EntrySink implements AtomSink {

   @Override
   public final SinkResult feedPage(Feed page) throws AtomSinkException {
      for (Entry e : page.entries()) {
         final SinkResult result = entry(e);

         if (result.action() == SinkAction.HALT) {
            return result;
         }
      }

      return AtomSinkResult.ok();
   }
}
