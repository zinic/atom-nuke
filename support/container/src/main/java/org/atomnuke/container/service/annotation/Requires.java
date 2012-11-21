package org.atomnuke.container.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks service requirements.
 *
 * Requirements may be specified in two ways:
 *
 * The first and preferred way is by using this annotation to private an
 * enumeration of the required service interface classses.
 *
 * Alternatively, for more complex requirements, a reference to a requires.xml
 * for this service located within its local classpath may be specified. To do
 * this, set the lookup value of this annotation to the correct location.
 *
 * @author zinic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Requires {

   Class[] value() default {};

   String lookup() default "";
}
