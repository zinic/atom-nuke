package org.atomnuke.pubsub;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class SubscriptionEventlet implements AtomEventlet {

   //TODO: Need HttpClient service

   @Override
   public void entry(Entry entry) throws AtomEventletException {
   }

   @Override
   public void init(AtomTaskContext contextObject) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
