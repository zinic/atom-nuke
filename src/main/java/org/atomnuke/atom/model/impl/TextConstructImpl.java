package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Summary;
import org.atomnuke.atom.model.TextConstruct;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Type;

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

   public String toString() {
      return value.toString();
   }
}
