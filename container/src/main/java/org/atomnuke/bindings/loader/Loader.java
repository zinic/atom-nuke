package org.atomnuke.bindings.loader;

import java.net.URI;
import org.atomnuke.bindings.BindingLoaderException;

/**
 *
 * @author zinic
 */
public interface Loader {

   void load(URI in) throws BindingLoaderException;
}
