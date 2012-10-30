package org.atomnuke.utilities.sinks;

import java.util.Map;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.SinkResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class JavaIdEchoSink implements AtomSink {

   private static final Logger LOG = LoggerFactory.getLogger(JavaIdEchoSink.class);

   @Override
   public SinkResult entry(Entry entry) throws AtomSinkException {
      if (entry.id() != null) {
         LOG.info("From Java: " + entry.id().toString());
      }

      return AtomSinkResult.ok();
   }

   @Override
   public SinkResult feedPage(Feed page) throws AtomSinkException {
      for (Entry entry : page.entries()) {
         entry(entry);
      }

      return AtomSinkResult.ok();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      LOG.info("Java ID echo sink initialized.");

      for (Map.Entry<String, String> param : tc.parameters().entrySet()) {
         LOG.info("Sink init for: " + toString() + ". Parameter, \"" + param.getKey() + "\" has value: " + param.getValue());
      }
   }

   @Override
   public void destroy() {
      LOG.info("Java ID echo sink destroyed.");
   }
}
