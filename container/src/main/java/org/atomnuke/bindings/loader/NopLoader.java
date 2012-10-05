package org.atomnuke.bindings.loader;

import java.net.URI;
import org.atomnuke.bindings.BindingLoaderException;

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
   public void load(URI in) throws BindingLoaderException {
   }
}
