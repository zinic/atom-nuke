package org.atomnuke.collectd;

import org.atomnuke.collectd.servlet.jetty.JettyServer;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.collectd.source.QueueSource;
import org.atomnuke.collectd.source.QueueSourceImpl;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class CollectdSource implements AtomSource {

   private static final Logger LOG = LoggerFactory.getLogger(CollectdSource.class);

   private final QueueSource queueSource;
   private final JettyServer jettyServer;

   public CollectdSource() {
      queueSource = new QueueSourceImpl();
      jettyServer = new JettyServer(8080, queueSource);
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      return queueSource.poll();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
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
   public void destroy() {
      try {
         jettyServer.stop();

         while (!jettyServer.isStopped() && !jettyServer.isFailed()) {
            Thread.sleep(100);
         }
      } catch (Exception ex) {
         LOG.error("Failed to destroy HTTPSource. Reason: " + ex.getMessage(), ex);
      }
   }
}
