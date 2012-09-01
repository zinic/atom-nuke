package org.atomnuke.atom.sax.impl;

import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.GeneratorBuilder;
import org.atomnuke.atom.model.builder.IconBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.LinkBuilder;
import org.atomnuke.atom.model.builder.LogoBuilder;
import org.atomnuke.atom.model.builder.RightsBuilder;
import org.atomnuke.atom.model.builder.SourceBuilder;
import org.atomnuke.atom.model.builder.SubtitleBuilder;
import org.atomnuke.atom.model.builder.TitleBuilder;
import org.atomnuke.atom.model.builder.UpdatedBuilder;
import org.atomnuke.atom.sax.DocumentContextManager;
import org.atomnuke.atom.sax.HandlerContext;
import org.atomnuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author zinic
 */
public class SourceHandler extends AtomHandler {

   public SourceHandler(AtomHandler delegate) {
      super(delegate);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.find(localName, AtomElement.SOURCE_ELEMENTS);

      if (currentElement == null) {
         return;
      }

      handleCommonElement(this, contextManager, currentElement, attributes);
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();

      if (!currentElement.elementName().equals(localName)) {
         return;
      }

      switch (currentElement) {
         case SOURCE:
            endSource(this, contextManager);
            break;

         case AUTHOR:
            endAuthor(contextManager);
            break;

         case GENERATOR:
            endGenerator(contextManager);
            break;

         case UPDATED:
            endUpdated(contextManager);
            break;

         case LINK:
            endLink(contextManager);
            break;

         case CATEGORY:
            endCategory(contextManager);
            break;

         case ID:
            endId(contextManager);
            break;

         case ICON:
            endIcon(contextManager);
            break;

         case LOGO:
            endLogo(contextManager);
            break;

         case RIGHTS:
            endRights(contextManager);
            break;

         case TITLE:
            endTitle(contextManager);
            break;

         case SUBTITLE:
            endSubtitle(contextManager);
            break;

         case NAME:
         case URI:
         case EMAIL:
            contextManager.pop();
            break;
      }
   }

   private static void endSource(SourceHandler self, DocumentContextManager contextManager) {
      final HandlerContext<SourceBuilder> sourceBuilder = contextManager.pop(SourceBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setSource(sourceBuilder.builder().build());

      self.releaseToParent();
   }

   private static void endAuthor(DocumentContextManager contextManager) {
      final HandlerContext<AuthorBuilder> personContext = contextManager.pop(AuthorBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().addAuthor(personContext.builder().build());
   }

   private static void endId(DocumentContextManager contextManager) {
      final HandlerContext<IdBuilder> idContext = contextManager.pop(IdBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setId(idContext.builder().build());
   }

   private static void endLogo(DocumentContextManager contextManager) {
      final HandlerContext<LogoBuilder> logoContext = contextManager.pop(LogoBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setLogo(logoContext.builder().build());
   }

   private static void endIcon(DocumentContextManager contextManager) {
      final HandlerContext<IconBuilder> iconContext = contextManager.pop(IconBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setIcon(iconContext.builder().build());
   }

   private static void endUpdated(DocumentContextManager contextManager) {
      final HandlerContext<UpdatedBuilder> updatedContext = contextManager.pop(UpdatedBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setUpdated(updatedContext.builder().build());
   }

   private static void endCategory(DocumentContextManager contextManager) {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().addCategory(category.builder().build());
   }

   private static void endLink(DocumentContextManager contextManager) {
      final HandlerContext<LinkBuilder> link = contextManager.pop(LinkBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().addLink(link.builder().build());
   }

   private static void endGenerator(DocumentContextManager contextManager) {
      final HandlerContext<GeneratorBuilder> generatorContext = contextManager.pop(GeneratorBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setGenerator(generatorContext.builder().build());
   }

   private static void endRights(DocumentContextManager contextManager) {
      final HandlerContext<RightsBuilder> textConstructContext = contextManager.pop(RightsBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setRights(textConstructContext.builder().build());
   }

   private static void endTitle(DocumentContextManager contextManager) {
      final HandlerContext<TitleBuilder> textConstructContext = contextManager.pop(TitleBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setTitle(textConstructContext.builder().build());
   }

   private static void endSubtitle(DocumentContextManager contextManager) {
      final HandlerContext<SubtitleBuilder> textConstructContext = contextManager.pop(SubtitleBuilder.class);
      contextManager.peek(SourceBuilder.class).builder().setSubtitle(textConstructContext.builder().build());
   }
}
