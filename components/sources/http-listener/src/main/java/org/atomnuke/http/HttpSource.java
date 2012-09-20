package org.atomnuke.http;

import org.atomnuke.servlet.jetty.JettyServer;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.QueueSource;
import org.atomnuke.source.QueueSourceImpl;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class HttpSource implements AtomSource {

   private final QueueSource queueSource;
   private final JettyServer jettyServer;

   public HttpSource() {
      queueSource = new QueueSourceImpl();
      jettyServer = new JettyServer(8080, queueSource);
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      return queueSource.poll();
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
      try {
         jettyServer.start();

         while (!jettyServer.isStarted()) {
            Thread.sleep(100);
         }
      } catch (Exception ex) {
         throw new InitializationException(ex.getMessage(), ex.getCause());
      }
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
      try {
         jettyServer.stop();

         while (!jettyServer.isStopped() && !jettyServer.isFailed()) {
            Thread.sleep(100);
         }
      } catch (Exception ex) {
         throw new DestructionException(ex.getMessage(), ex.getCause());
      }
   }
}
