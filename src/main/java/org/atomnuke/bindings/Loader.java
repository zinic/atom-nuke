package org.atomnuke.bindings;

import java.io.InputStream;

/**
 *
 * @author zinic
 */
public interface Loader {

   void load(String href, InputStream in) throws BindingLoaderException;
}
