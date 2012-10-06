package org.atomnuke.container.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atomnuke.container.service.construct.Constructor;
import org.atomnuke.container.service.construct.reflection.GenericReflectionConstructor;

/**
 *
 * @author zinic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NukeService {
}