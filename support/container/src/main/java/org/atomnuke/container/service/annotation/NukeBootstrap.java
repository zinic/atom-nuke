package org.atomnuke.container.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This interface marks a service as a bootstrap target. This service will be
 * initialized at container initialization and will be discovered at runtime.
 *
 * @author zinic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NukeBootstrap {
}
