package org.atomnuke.examples.sinks;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkResult;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.DestructionException;
import org.atomnuke.util.lifecycle.InitializationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.atomnuke.atom.io.AtomWriterFactory;

/**
 *
 * @author zinic
 */
public class HDFSFeedSink implements AtomSink {

   private final Configuration configuration;
   private final Path targetPath;
   private final String feedName;
   private final AtomWriterFactory writerFactory;

   private SequenceFile.Writer fileWriter;
   private boolean writeHeader;
   private FileSystem hdfs;

   public HDFSFeedSink(String feedName, AtomWriterFactory writerFactory) {
      this.feedName = feedName;
      this.writerFactory = writerFactory;

      targetPath = new Path("/data/atom/" + feedName);

      configuration = new Configuration();
      configuration.set("fs.default.name", "hdfs://namenode:9000");

      writeHeader = false;
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      try {
         hdfs = FileSystem.get(configuration);

         writeHeader = !hdfs.exists(targetPath);
         fileWriter = SequenceFile.createWriter(hdfs, configuration, targetPath, Text.class, Text.class);
      } catch (IOException ioe) {
         throw new InitializationException(ioe);
      }
   }

   @Override
   public void destroy() {
      try {
         fileWriter.close();
         hdfs.close();
      } catch (IOException ioe) {
         throw new DestructionException(ioe);
      }
   }

   @Override
   public AtomSinkResult entry(Entry entry) throws AtomSinkException {
      try {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         writerFactory.getInstance().write(baos, entry);
         append(new Text(entry.id().toString()), new Text(baos.toByteArray()));
      } catch (Exception ioe) {
         throw new AtomSinkException(ioe);
      }

      return AtomSinkResult.ok();
   }

   @Override
   public AtomSinkResult feedPage(Feed page) throws AtomSinkException {
      try {
         if (writeHeader) {
            writeFeedHeader(page);
         }

         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         for (Entry e : page.entries()) {
            writerFactory.getInstance().write(baos, page);
            append(new Text(e.id().toString()), new Text(baos.toByteArray()));
         }
      } catch (Exception ioe) {
         throw new AtomSinkException(ioe);
      }

      return AtomSinkResult.ok();
   }

   private void append(String key, String value) throws IOException {
      append(new Text(key), new Text(value));
   }

   private void append(Text key, Text value) throws IOException {
      fileWriter.append(key, value);

      final BufferedWriter lookaside = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/home/zinic/atom.txt"), true)));
      lookaside.write(key.toString());
      lookaside.write("\n");
      lookaside.write(value.toString());
      lookaside.write("\n\n");

      lookaside.close();
   }

   private void writeVariable(String key, String value, StringBuilder builder) {
      builder.append("\"");
      builder.append(key);
      builder.append("\":\"");
      builder.append(value);
      builder.append("\"");
   }

   private void writeFeedHeader(Feed page) throws IOException {
      writeHeader = false;

      final StringBuilder header = new StringBuilder("{");
      writeVariable("id", page.id().toString(), header);
      header.append(",");
      writeVariable("title", page.title().toString(), header);
      header.append("}");

      append(feedName + "-metadata", header.toString());
   }
}
