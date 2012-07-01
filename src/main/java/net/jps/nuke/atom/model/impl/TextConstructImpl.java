package net.jps.nuke.atom.model.impl;

import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Subtitle;
import net.jps.nuke.atom.model.Summary;
import net.jps.nuke.atom.model.TextConstruct;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Type;

/**
 *
 * @author zinic
 */
public abstract class TextConstructImpl extends AtomCommonAttributesImpl implements TextConstruct, Rights, Subtitle, Title, Summary {

   protected Type type;
   protected StringBuilder value;

   public Type type() {
      return type;
   }

   public String value() {
      return value.toString();
   }
}
