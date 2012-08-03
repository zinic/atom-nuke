package net.jps.nuke.bindings;

import java.io.File;
import java.io.OutputStream;

/**
 *
 * @author zinic
 */
public interface InterpreterContext {

    LoadResult load(File file);

    void setStdErr(OutputStream outputStream);
    
    void setStdOut(OutputStream outputStream);

    <T> ObjectFactory<T> newObjectFactory(Class<T> objectClass);
}
