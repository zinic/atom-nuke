package org.atomnuke.aggregator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import net.jps.jx.JsonReader;
import net.jps.jx.JsonWriter;
import net.jps.jx.JxFactory;
import net.jps.jx.jackson.JacksonJxFactory;
import org.atomnuke.aggregator.collectd.CollectdStatisticValue;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContentBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.fallout.source.queue.QueueSource;
import org.atomnuke.fallout.source.queue.EntryQueueImpl;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.SinkResult;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class AggregatorSink implements AtomSink, AtomSource {

   private static final Logger LOG = LoggerFactory.getLogger(AggregatorSink.class);
   private static final JxFactory JX_FACTORY;

   static {
      JxFactory jsonFactory = null;

      try {
         jsonFactory = new JacksonJxFactory();
      } catch (DatatypeConfigurationException configurationException) {
         LOG.error(configurationException.getMessage(), configurationException);
      }

      JX_FACTORY = jsonFactory;
   }

   private final JsonReader<CollectdStatisticValue> contentReader = JX_FACTORY.newReader(CollectdStatisticValue.class);
   private final JsonWriter<CollectdStatisticValue> documentWriter = JX_FACTORY.newWriter(CollectdStatisticValue.class);

   private final Map<String, Double> runningValues;
   private final QueueSource queueSource;

   public AggregatorSink() {
      queueSource = new EntryQueueImpl();
      runningValues = new HashMap<String, Double>();
   }

   private synchronized Double getValue(String key) {
      return runningValues.get(key);
   }

   private synchronized void setValue(String key, Double valueToAdd) {
      runningValues.put(key, valueToAdd);
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      return queueSource.poll();
   }

   @Override
   public SinkResult entry(Entry entry) throws AtomSinkException {
      try {
         for (Category category : entry.categories()) {
            if (category.scheme().equals("collectd")) {
               final CollectdStatisticValue statisticalValue = contentReader.read(new ByteArrayInputStream(entry.content().toString().getBytes()));

               if (category.term().startsWith("fallout-test-n01.jpserver.net/memory/")) {
                  setValue(category.term(), statisticalValue.getValueAsDouble());
               }

               if (category.term().startsWith("fallout-test-n01.jpserver.net/cpu/")) {
                  setValue(category.term(), statisticalValue.getValueAsDouble());
               }
            }
         }

         queueSource.put(entry);
         emitMemory();
         emitCpu();
      } catch (Exception ex) {
         LOG.error(ex.getMessage(), ex);
      }

      return AtomSinkResult.ok();
   }

   private synchronized void emitCpu() throws Exception {
      double usage = 0, idle = 0;

      for (Map.Entry<String, Double> valueSet : runningValues.entrySet()) {
         if (valueSet.getKey().startsWith("fallout-test-n01.jpserver.net/cpu/")) {
            if (valueSet.getKey().endsWith("user")) {
               usage += valueSet.getValue();
            } else {
               idle += valueSet.getValue();
            }
         }
      }

      if (usage <= 0 && idle <= 0) {
         return;
      }

      final EntryBuilder memoryEntry = new EntryBuilder();
      memoryEntry.addCategory(new CategoryBuilder().setScheme("collectd").setTerm("fallout-test-n01.jpserver.net/cpu/percent_used").build());

      final CollectdStatisticValue statisticalValue = new CollectdStatisticValue();
      statisticalValue.setTimestamp(String.valueOf(System.currentTimeMillis()));
      statisticalValue.setValue(String.valueOf(usage <= 0 ? 0 : 100 * idle / (idle + usage)));

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      documentWriter.write(statisticalValue, baos);

      final String content = new String(baos.toByteArray());
      memoryEntry.setContent(new ContentBuilder().setType("application/json").setValue(content).build());

      queueSource.put(memoryEntry.build());
   }

   private void emitMemory() throws Exception {
      final Double memoryUsed = getValue("fallout-test-n01.jpserver.net/memory/used");
      final Double memoryBuffered = getValue("fallout-test-n01.jpserver.net/memory/buffered");
      final Double memoryCached = getValue("fallout-test-n01.jpserver.net/memory/cached");
      final Double memoryFree = getValue("fallout-test-n01.jpserver.net/memory/free");

      if (memoryUsed == null  || memoryBuffered == null  || memoryCached == null  || memoryFree == null ) {
         LOG.info("Still waiting for memory stats to collect...");
         return;
      }

      final double totalMemory = memoryUsed + memoryBuffered + memoryCached + memoryFree;
      final double totalUsed = memoryUsed + memoryBuffered + memoryCached;

      final EntryBuilder memoryEntry = new EntryBuilder();
      memoryEntry.addCategory(new CategoryBuilder().setScheme("collectd").setTerm("fallout-test-n01.jpserver.net/memory/percent_used").build());

      final CollectdStatisticValue statisticalValue = new CollectdStatisticValue();
      statisticalValue.setTimestamp(String.valueOf(System.currentTimeMillis()));
      statisticalValue.setValue(String.valueOf(
              memoryFree <= 0 ? 0 : 100 * totalUsed / totalMemory));

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      documentWriter.write(statisticalValue, baos);

      final String content = new String(baos.toByteArray());
      memoryEntry.setContent(new ContentBuilder().setType("application/json").setValue(content).build());

      queueSource.put(memoryEntry.build());
   }

   @Override
   public SinkResult feedPage(Feed page) throws AtomSinkException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
