package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Logo;

/**
 *
 * @author zinic
 */
public class LogoBuilder extends SimpleContentBuilder<LogoBuilder, Logo, TextContent> {

   public LogoBuilder() {
      super(LogoBuilder.class, new TextContent());
   }

   public LogoBuilder(Logo copyConstruct) {
      super(LogoBuilder.class, new TextContent(), copyConstruct);
   }
}