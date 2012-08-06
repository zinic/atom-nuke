package org.atomnuke.bindings.loader;

import java.io.InputStream;
import org.atomnuke.bindings.BindingLoaderException;

/**
 *
 * @author zinic
 */
public interface Loader {

   void load(InputStream in) throws BindingLoaderException;
}
