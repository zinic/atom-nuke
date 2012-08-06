package org.atomnuke.bindings;

import java.io.InputStream;

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
   public void load(String href, InputStream in) throws BindingLoaderException {
   }
}
