package org.atomnuke.control.eventlet;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class AtomCollectorPublisher implements AtomEventlet, AtomSource {

   private final List<Entry> eventQueue;

   public AtomCollectorPublisher() {
      eventQueue = new LinkedList<Entry>();
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      eventQueue.add(entry);
   }

   @Override
   public void init(AtomTaskContext contextObject) throws InitializationException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void destroy() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}
