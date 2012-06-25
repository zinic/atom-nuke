package net.jps.nuke.listener.hadoop;

import net.jps.nuke.listener.FeedListener;
import net.jps.nuke.listener.ListenerResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
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

   private SequenceFile.Writer fileWriter;
   private boolean writeHeader;
   private FileSystem hdfs;

   public HDFSFeedListener(String feedName) {
      this.feedName = feedName;
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

         for (Entry e : page.getEntries()) {
            final StringWriter stringWriter = new StringWriter();
            e.writeTo(stringWriter);

            append(e.getId().toString(), stringWriter.toString());
         }
      } catch (IOException ioe) {
         ioe.printStackTrace(System.err);
      }

      if (page.getEntries().size() > 0) {
         for (Link link : page.getLinks()) {
            if (link.getRel().equals("previous")) {
               return ListenerResult.follow(link);
            }
         }
      }
      
      return ListenerResult.halt("End of feed.");
   }

   private void append(String key, String value) throws IOException {
      fileWriter.append(new Text(key), new Text(value));
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
      writeVariable("id", page.getId().toString(), header);
      header.append(",");
      writeVariable("title", page.getTitle(), header);
      header.append("}");

      append(feedName + "-metadata", header.toString());
   }
}
