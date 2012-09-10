package org.atomnuke.bindings.loader;

import java.io.InputStream;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.Loader;

/**
 *
 * @author zinic
 */
public final class NopLoader implements Loader {

   private static final Loader INSTANCE = new NopLoader();

   public static Loader instance() {
      return INSTANCE;
   }

   private NopLoader() {
   }

   @Override
   public void load(InputStream in) throws BindingLoaderException {
   }
}
