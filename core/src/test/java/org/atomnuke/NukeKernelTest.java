package org.atomnuke;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.builder.FeedBuilder;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.SinkResult;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.AtomSourceResultImpl;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.util.TimeValue;
import org.junit.Test;

/**
 *
 * @author zinic
 */
public class NukeKernelTest {

   @Test
   public void nukeShakedownTest() throws Exception {
      final NukeKernel nukeKernel = new NukeKernel();
      final AtomicLong eventsProcessed = new AtomicLong(0);

      final AtomSource source = new AtomSource() {
         @Override
         public AtomSourceResult poll() throws AtomSourceException {
            return new AtomSourceResultImpl(new FeedBuilder().build());
         }

         @Override
         public void init(AtomTaskContext tc) throws InitializationException {
         }

         @Override
         public void destroy() {
         }
      };

      final AtomSink sink = new AtomSink() {
         @Override
         public SinkResult entry(Entry entry) throws AtomSinkException {
            eventsProcessed.incrementAndGet();

            return AtomSinkResult.ok();
         }

         @Override
         public SinkResult feedPage(Feed page) throws AtomSinkException {
            eventsProcessed.incrementAndGet();

            return AtomSinkResult.ok();
         }

         @Override
         public void init(AtomTaskContext tc) throws InitializationException {
         }

         @Override
         public void destroy() {
         }
      };

      for (int taskId = 1; taskId <= 30; taskId++) {
         final AtomTask task = nukeKernel.follow(source, new TimeValue(10 * taskId, TimeUnit.MICROSECONDS));
         task.addSink(new InstanceContextImpl<AtomSink>(NopInstanceEnvironment.getInstance(), sink));
      }

      nukeKernel.start();

      Thread.sleep(1000);

      nukeKernel.destroy();

      System.out.println("Processed " + eventsProcessed.get() + " feed events in one second.");
   }
}
