package org.atomnuke.bindings.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.atomnuke.bindings.BindingContext;
import org.atomnuke.bindings.BindingLoaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class DirectoryLoaderManager {

   private static final Logger LOG = LoggerFactory.getLogger(DirectoryLoaderManager.class);
   
   private final List<BindingContext> bindingContexts;
   private final File libraryDirectory;

   public DirectoryLoaderManager(String libraryDirectory, List<BindingContext> bindingContexts) {
      this.libraryDirectory = new File(libraryDirectory);
      this.bindingContexts = bindingContexts;
   }

   public void load() throws BindingLoaderException {
      if (!libraryDirectory.exists()) {
         if (!libraryDirectory.mkdirs()) {
            throw new BindingLoaderException("Unable to make library directory: " + libraryDirectory.getAbsolutePath());
         }
      }
      
      if (!libraryDirectory.isDirectory()) {
         throw new BindingLoaderException(libraryDirectory.getAbsolutePath() + " is not a valid library directory.");
      }

      for (File file : libraryDirectory.listFiles()) {
         if (file.isDirectory()) {
            // Ignore this for now
            continue;
         }

         final String fileName = file.getName();

         for (BindingContext context : bindingContexts) {
            boolean load = false;

            for (String extension : context.language().fileExtensions()) {
               if (fileName.endsWith(extension)) {
                  load = true;
                  break;
               }
            }

            if (load) {
               try {
                  final FileInputStream fin = new FileInputStream(file);
                  context.loader().load(fin);
                  fin.close();

                  LOG.info("Loaded file: " + file.getAbsolutePath());

                  break;
               } catch (IOException ioe) {
                  throw new BindingLoaderException("I/O Error: " + ioe.getMessage(), ioe);
               }
            }
         }
      }
   }
}
