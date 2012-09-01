package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.impl.TextContent;

/**
 *
 * @author zinic
 */
public class LogoBuilder extends SimpleContentBuilder<LogoBuilder, Logo, TextContent> {

   public LogoBuilder() {
      super(LogoBuilder.class, new TextContent());
   }
}