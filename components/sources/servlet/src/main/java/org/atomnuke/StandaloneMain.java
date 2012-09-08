package org.atomnuke;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.ListenerResult;
import org.atomnuke.servlet.jetty.JettyServer;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class StandaloneMain {

   public static void main(String[] args) throws Exception {
      final JettyServer jettyServer = new JettyServer(8080, new AtomListener() {
         @Override
         public ListenerResult entry(Entry entry) throws AtomListenerException {
            System.out.println("Entry recieved: " + entry.id().toString());

            return AtomListenerResult.ok();
         }

         @Override
         public ListenerResult feedPage(Feed page) throws AtomListenerException {
            return AtomListenerResult.ok();
         }

         @Override
         public void init(TaskContext tc) throws InitializationException {
         }

         @Override
         public void destroy(TaskContext tc) throws DestructionException {
         }
      });

      jettyServer.start();
   }
}
