package org.atomnuke.bindings.ear;

import com.rackspace.papi.commons.util.classloader.ear.EarClassLoaderContext;
import com.rackspace.papi.commons.util.classloader.ear.EarUnpacker;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class EarLoader implements Loader {

   private static final Logger LOG = LoggerFactory.getLogger(EarLoader.class);

   private final Map<String, EarClassLoaderContext> loadedPackages;
   private final EarUnpacker earUnpacker;

   public EarLoader(File deployDirectory) {
      loadedPackages = new HashMap<String, EarClassLoaderContext>();
      earUnpacker = new EarUnpacker(deployDirectory);
   }

   public Map<String, EarClassLoaderContext> getLoadedPackages() {
      return loadedPackages;
   }

   @Override
   public void load(InputStream in) throws BindingLoaderException {
      try {
         final EarClassLoaderContext ctx = earUnpacker.read(new NukeEarArchiveEntryHelper(Thread.currentThread().getContextClassLoader(), earUnpacker.getDeploymentDirectory()), in);
         loadedPackages.put(ctx.getEarDescriptor().getApplicationName(), ctx);
      } catch (IOException ioe) {
         LOG.error(ioe.getMessage(), ioe);
      }
   }
}
