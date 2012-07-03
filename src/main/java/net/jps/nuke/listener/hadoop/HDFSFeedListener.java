package net.jps.nuke.listener.hadoop;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import net.jps.nuke.listener.FeedListener;
import net.jps.nuke.listener.ListenerResult;
import java.io.IOException;
import java.io.OutputStreamWriter;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.Writer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

/**
 *
 * @author zinic
 */
public class HDFSFeedListener implements FeedListener {

   private final Configuration configuration;
   private final Path targetPath;
   private final String feedName;
   private final Writer writer;
   
   private SequenceFile.Writer fileWriter;
   private boolean writeHeader;
   private FileSystem hdfs;

   public HDFSFeedListener(String feedName, Writer writer) {
      this.feedName = feedName;
      this.writer = writer;

      targetPath = new Path("/data/atom/" + feedName);

      configuration = new Configuration();
      configuration.set("fs.default.name", "hdfs://namenode:9000");

      writeHeader = false;
   }

   public void init() {
      try {
         hdfs = FileSystem.get(configuration);

         writeHeader = !hdfs.exists(targetPath);
         fileWriter = SequenceFile.createWriter(hdfs, configuration, targetPath, Text.class, Text.class);
      } catch (IOException ioe) {
         ioe.printStackTrace(System.err);
         throw new RuntimeException(ioe);
      }
   }

   public void destroy() {
      try {
         fileWriter.close();
         hdfs.close();
      } catch (IOException ioe) {
         ioe.printStackTrace(System.err);
         throw new RuntimeException(ioe);
      }
   }

   public ListenerResult readPage(Feed page) {
      try {
         if (writeHeader) {
            writeFeedHeader(page);
         }

         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         for (Entry e : page.entries()) {
            writer.write(baos, page);
            append(new Text(e.id().value()), new Text(baos.toByteArray()));
         }
      } catch (Exception ioe) {
         ioe.printStackTrace(System.err);
      }

      for (Link link : page.links()) {
         if (link.rel().equals("previous")) {
            return ListenerResult.follow(link);
         }
      }

      return ListenerResult.halt("End of feed.");
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
      writeVariable("id", page.id().value(), header);
      header.append(",");
      writeVariable("title", page.title().value(), header);
      header.append("}");

      append(feedName + "-metadata", header.toString());
   }
}
