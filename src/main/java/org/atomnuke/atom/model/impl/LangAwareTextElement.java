package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Logo;

/**
 *
 * @author zinic
 */
public abstract class LangAwareTextElement extends AtomCommonAttributesImpl implements Id, Icon, Logo {

   protected StringBuilder value;

   public String toString() {
      return value.toString();
   }
}
