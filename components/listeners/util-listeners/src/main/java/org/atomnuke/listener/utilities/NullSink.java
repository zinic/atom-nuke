package org.atomnuke.listener.utilities;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.ListenerResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class NullSink implements AtomListener {

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
      return AtomListenerResult.ok();
   }

   @Override
   public ListenerResult feedPage(Feed page) throws AtomListenerException {
      return AtomListenerResult.ok();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
