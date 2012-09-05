package org.atomnuke.examples.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.atomnuke.atom.Writer;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.stax.StaxAtomWriter;
import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class FeedFileWriterHandler implements AtomEventlet {

   private static final byte[] NEWLINE = "\n".getBytes();
   
   private final Writer atomWriter;
   private final File feedFile;
   
   private FileOutputStream fileOutput;

   public FeedFileWriterHandler(File feedFile) {
      this.feedFile = feedFile;

      atomWriter = new StaxAtomWriter();
   }

   private synchronized void write(byte[] entry) throws XMLStreamException, IOException {
      fileOutput.write(entry);
      fileOutput.write(NEWLINE);
      fileOutput.flush();
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
      try {
         fileOutput = new FileOutputStream(feedFile);
      } catch (IOException ioe) {
         throw new InitializationException("IOException caught while trying to open feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
      try {
         fileOutput.flush();
         fileOutput.close();
      } catch (IOException ioe) {
         throw new DestructionException("IOException caught while trying to close feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();

      try {
         atomWriter.write(baos, entry);
         write(baos.toByteArray());
      } catch (XMLStreamException xmlse) {
         throw new AtomEventletException("XMLStreamException caught while trying to write to feed file \"" + feedFile.getAbsolutePath() + "\"", xmlse);
      } catch (IOException ioe) {
         throw new AtomEventletException("IOException caught while trying to write to feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }
}
