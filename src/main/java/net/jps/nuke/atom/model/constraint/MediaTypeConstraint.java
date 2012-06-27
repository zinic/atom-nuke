package net.jps.nuke.atom.model.constraint;

/**
 *
 * @author zinic
 */
public class MediaTypeConstraint implements ValueConstraint {

   public String name() {
      return "atomMediaType";
   }

   public String pattern() {
      return ".+/.+";
   }
}
