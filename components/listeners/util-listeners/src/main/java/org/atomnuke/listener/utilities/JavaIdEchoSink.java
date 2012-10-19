package org.atomnuke.listener.utilities;

import java.util.Map;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.ListenerResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class JavaIdEchoSink implements AtomListener {

   private static final Logger LOG = LoggerFactory.getLogger(JavaIdEchoSink.class);

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
      if (entry.id() != null) {
         LOG.info("From Java: " + entry.id().toString());
      }

      return AtomListenerResult.ok();
   }

   @Override
   public ListenerResult feedPage(Feed page) throws AtomListenerException {
      for (Entry entry : page.entries()) {
         entry(entry);
      }

      return AtomListenerResult.ok();
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
