package net.jps.nuke.atom.sax.impl;

import net.jps.nuke.atom.ParserResultImpl;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.ContentBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.LangAwareTextElementBuilder;
import net.jps.nuke.atom.model.builder.LinkBuilder;
import net.jps.nuke.atom.model.builder.PersonConstructBuilder;
import net.jps.nuke.atom.model.builder.SourceBuilder;
import net.jps.nuke.atom.model.builder.TextConstructBuilder;
import net.jps.nuke.atom.model.builder.DateConstructBuilder;
import net.jps.nuke.atom.sax.DocumentContextManager;
import net.jps.nuke.atom.sax.HandlerContext;
import net.jps.nuke.atom.sax.MixedContentHandler;
import net.jps.nuke.atom.xml.AtomElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author zinic
 */
public class EntryHandler extends AtomHandler {

   public EntryHandler(AtomHandler delegate) {
      super(delegate);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      final AtomElement currentElement = AtomElement.find(asLocalName(qName, localName), AtomElement.ENTRY_ELEMENTS);

      if (currentElement == null) {
         // TODO:Implement - Error case. Unknown element...
         return;
      }

      switch (currentElement) {
         case SOURCE:
            startSource(this, contextManager, attributes);
            break;

         case AUTHOR:
         case CONTRIBUTOR:
            startPersonConstruct(contextManager, currentElement, attributes);
            break;

         case CONTENT:
            startContent(this, contextManager, currentElement, attributes);
            break;

         case CATEGORY:
            startCategory(contextManager, attributes);
            break;

         case LINK:
            startLink(contextManager, attributes);
            break;

         case ID:
            startLangAwareTextElement(contextManager, currentElement, attributes);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(contextManager, currentElement);
            break;

         case PUBLISHED:
         case UPDATED:
            startDateConstruct(contextManager, currentElement, attributes);
            break;

         case RIGHTS:
         case TITLE:
         case SUMMARY:
            startTextConstruct(this, contextManager, currentElement, attributes);
            break;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();
      final String elementEnding = asLocalName(qName, localName);

      if (!currentElement.elementName().equals(elementEnding)) {
         return;
      }

      switch (currentElement) {
         case ENTRY:
            endEntry(this, contextManager, result);
            break;

         case AUTHOR:
            endAuthor(contextManager);
            break;

         case CONTRIBUTOR:
            endContributor(contextManager);
            break;

         case PUBLISHED:
            endPublished(contextManager);
            break;

         case UPDATED:
            endUpdated(contextManager);
            break;

         case CONTENT:
            endContent(contextManager);
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

         case RIGHTS:
            endRights(contextManager);
            break;

         case TITLE:
            endTitle(contextManager);
            break;

         case SUMMARY:
            endSummary(contextManager);
            break;

         case NAME:
         case URI:
         case EMAIL:
            contextManager.pop();
            break;
      }
   }

   private static void startSource(EntryHandler self, DocumentContextManager contextManager, Attributes attributes) {
      final SourceBuilder sourceBuilder = new SourceBuilder();

      sourceBuilder.setBase(toUri(attributes.getValue("base")));
      sourceBuilder.setLang(attributes.getValue("lang"));

      contextManager.push(AtomElement.SOURCE, sourceBuilder);
      self.delegateTo(new SourceHandler(self));
   }

   private static void startContent(EntryHandler self, DocumentContextManager contextManager, AtomElement element, Attributes attributes) {
      final ContentBuilder contentBuilder = new ContentBuilder();

      contentBuilder.setBase(toUri(attributes.getValue("base")));
      contentBuilder.setLang(attributes.getValue("lang"));
      contentBuilder.setType(attributes.getValue("type"));
      contentBuilder.setSrc(attributes.getValue("src"));

      contextManager.push(element, contentBuilder);
      self.delegateTo(new MixedContentHandler(contentBuilder.getValueBuilder(), self));
   }

   private static void endEntry(EntryHandler self, DocumentContextManager contextManager, ParserResultImpl result) {
      final HandlerContext<EntryBuilder> entryContext = contextManager.pop(EntryBuilder.class);

      if (contextManager.hasContext()) {
         final HandlerContext<FeedBuilder> feedBuilderContext = contextManager.peek(FeedBuilder.class);
         feedBuilderContext.builder().addEntry(entryContext.builder());
      } else {
         result.setEntryBuilder(entryContext.builder());
      }

      self.releaseToParent();
   }

   private static void endAuthor(DocumentContextManager contextManager) {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addAuthor(personContext.builder());
   }

   private static void endContributor(DocumentContextManager contextManager) {
      final HandlerContext<PersonConstructBuilder> personContext = contextManager.pop(PersonConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addContributor(personContext.builder());
   }

   private static void endId(DocumentContextManager contextManager) {
      final HandlerContext<LangAwareTextElementBuilder> idContext = contextManager.pop(LangAwareTextElementBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setId(idContext.builder());
   }

   private static void endUpdated(DocumentContextManager contextManager) {
      final HandlerContext<DateConstructBuilder> updatedContext = contextManager.pop(DateConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setUpdated(updatedContext.builder());
   }

   private static void endPublished(DocumentContextManager contextManager) {
      final HandlerContext<DateConstructBuilder> publishedContext = contextManager.pop(DateConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setPublished(publishedContext.builder());
   }

   private static void endContent(DocumentContextManager contextManager) {
      final HandlerContext<ContentBuilder> content = contextManager.pop(ContentBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setContent(content.builder());
   }

   private static void endCategory(DocumentContextManager contextManager) {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addCategory(category.builder());
   }

   private static void endLink(DocumentContextManager contextManager) {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addLink(category.builder());
   }

   private static void endRights(DocumentContextManager contextManager) {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setRights(textConstructContext.builder());
   }

   private static void endSummary(DocumentContextManager contextManager) {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setSummary(textConstructContext.builder());
   }

   private static void endTitle(DocumentContextManager contextManager) {
      final HandlerContext<TextConstructBuilder> textConstructContext = contextManager.pop(TextConstructBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setTitle(textConstructContext.builder());
   }
}
