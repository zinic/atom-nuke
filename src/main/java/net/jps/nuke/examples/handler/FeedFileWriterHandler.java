package net.jps.nuke.examples.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import net.jps.nuke.atom.Writer;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.stax.StaxAtomWriter;
import net.jps.nuke.listener.eps.handler.AtomEventHandler;
import net.jps.nuke.listener.eps.handler.AtomEventHandlerException;

/**
 *
 * @author zinic
 */
public class FeedFileWriterHandler implements AtomEventHandler {

   private static final byte[] NEWLINE = "\n".getBytes();
   
   private final Writer atomWriter;
   private final File feedFile;
   private FileOutputStream fileOutput;

   public FeedFileWriterHandler(File feedFile) {
      this.feedFile = feedFile;
      
      atomWriter = new StaxAtomWriter();
   }

   @Override
   public void init() throws AtomEventHandlerException {
      try {
         fileOutput = new FileOutputStream(feedFile);
      } catch (IOException ioe) {
         throw new AtomEventHandlerException("IOException caught while trying to open feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }

   @Override
   public void destroy() throws AtomEventHandlerException {
      try {
         fileOutput.close();
      } catch (IOException ioe) {
         throw new AtomEventHandlerException("IOException caught while trying to close feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }

   @Override
   public void entry(Entry entry) throws AtomEventHandlerException {
      try {
         atomWriter.write(fileOutput, entry);
         fileOutput.write(NEWLINE);
      } catch (XMLStreamException xmlse) {
         throw new AtomEventHandlerException("XMLStreamException caught while trying to write to feed file \"" + feedFile.getAbsolutePath() + "\"", xmlse);
      } catch (IOException ioe) {
         throw new AtomEventHandlerException("IOException caught while trying to write to feed file \"" + feedFile.getAbsolutePath() + "\"", ioe);
      }
   }
}
