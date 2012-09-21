package org.atomnuke.listener.utilities;

import java.util.Map;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.ListenerResult;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class IdEchoSink implements AtomListener {

   private static final Logger LOG = LoggerFactory.getLogger(IdEchoSink.class);

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
      if (entry.id() != null) {
         LOG.info(entry.id().toString());
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
   public void init(TaskContext tc) throws InitializationException {
      for (Map.Entry<String, String> param : tc.instanceParameters().entrySet()) {
         LOG.info("Sink init for: " + toString() + ". Parameter, \"" + param.getKey() + "\" has value: " + param.getValue());
      }
   }

   @Override
   public void destroy() throws DestructionException {
      LOG.info("Echo sink destroyed.");
   }
}
