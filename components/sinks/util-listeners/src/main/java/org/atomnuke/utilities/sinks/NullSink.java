package org.atomnuke.utilities.sinks;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.SinkResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class NullSink implements AtomSink {

   @Override
   public SinkResult entry(Entry entry) throws AtomSinkException {
      return AtomSinkResult.ok();
   }

   @Override
   public SinkResult feedPage(Feed page) throws AtomSinkException {
      return AtomSinkResult.ok();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
