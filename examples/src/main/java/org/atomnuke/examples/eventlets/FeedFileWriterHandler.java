package org.atomnuke.examples.eventlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.atomnuke.atom.io.AtomWriteException;
import org.atomnuke.atom.io.AtomWriterFactory;
import org.atomnuke.atom.io.writer.stax.StaxAtomWriterFactory;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.DestructionException;
import org.atomnuke.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class FeedFileWriterHandler implements AtomEventlet {

   private static final byte[] NEWLINE = "\n".getBytes();

   private final AtomWriterFactory atomWriterFactory;
   private final File feedFile;

   private FileOutputStream fileOutput;

   public FeedFileWriterHandler(File feedFile) {
      this.feedFile = feedFile;

      atomWriterFactory = new StaxAtomWriterFactory();
   }

   private synchronized void write(byte[] entry) throws AtomWriteException, IOException {
      fileOutput.write(entry);
      fileOutput.write(NEWLINE);
      fileOutput.flush();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      try {
         fileOutput = new FileOutputStream(feedFile);
      } catch (IOException ioe) {
         throw new InitializationException("IOException caught while trying to open feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }

   @Override
   public void destroy() {
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
         atomWriterFactory.getInstance().write(baos, entry);
         write(baos.toByteArray());
      } catch (AtomWriteException awe) {
         throw new AtomEventletException("Exception caught while trying to write to feed file \"" + feedFile.getAbsolutePath() + "\"", awe);
      } catch (IOException ioe) {
         throw new AtomEventletException("IOException caught while trying to write to feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }
}
