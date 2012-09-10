package org.atomnuke.atom.io.reader.sax;

import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContentBuilder;
import org.atomnuke.atom.model.builder.ContributorBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.FeedBuilder;
import org.atomnuke.atom.model.builder.LinkBuilder;
import org.atomnuke.atom.model.builder.SourceBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.PublishedBuilder;
import org.atomnuke.atom.model.builder.RightsBuilder;
import org.atomnuke.atom.model.builder.SummaryBuilder;
import org.atomnuke.atom.model.builder.TitleBuilder;
import org.atomnuke.atom.model.builder.UpdatedBuilder;
import org.atomnuke.atom.xml.AtomElement;
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
      final AtomElement currentElement = AtomElement.find(localName, AtomElement.ENTRY_ELEMENTS);

      if (currentElement == null) {
         // TODO:Implement - Error case. Unknown element...
         return;
      }

      switch (currentElement) {
         case SOURCE:
            startSource(this, contextManager, attributes);
            break;

         case AUTHOR:
            startPersonConstruct(new AuthorBuilder(), contextManager, currentElement, attributes);
            break;

         case CONTRIBUTOR:
            startPersonConstruct(new ContributorBuilder(), contextManager, currentElement, attributes);
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
            startSimpleContentElement(new IdBuilder(), contextManager, currentElement, attributes);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(contextManager, currentElement);
            break;

         case PUBLISHED:
            startDateConstruct(new PublishedBuilder(), contextManager, currentElement, attributes);
            break;

         case UPDATED:
            startDateConstruct(new UpdatedBuilder(), contextManager, currentElement, attributes);
            break;

         case RIGHTS:
            startTypedContent(new RightsBuilder(), this, contextManager, currentElement, attributes);
            break;

         case TITLE:
            startTypedContent(new TitleBuilder(), this, contextManager, currentElement, attributes);
            break;

         case SUMMARY:
            startTypedContent(new SummaryBuilder(), this, contextManager, currentElement, attributes);
            break;
      }
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      final AtomElement currentElement = contextManager.peek().getElementDef();

      if (!currentElement.elementName().equals(localName)) {
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
      self.delegateTo(new MixedContentHandler(contentBuilder, self));
   }

   private static void endEntry(EntryHandler self, DocumentContextManager contextManager, SaxAtomReaderResult result) {
      final HandlerContext<EntryBuilder> entryContext = contextManager.pop(EntryBuilder.class);

      if (contextManager.hasContext()) {
         final HandlerContext<FeedBuilder> feedBuilderContext = contextManager.peek(FeedBuilder.class);
         feedBuilderContext.builder().addEntry(entryContext.builder().build());
      } else {
         result.setEntryResult(entryContext.builder().build());
      }

      self.releaseToParent();
   }

   private static void endAuthor(DocumentContextManager contextManager) {
      final HandlerContext<AuthorBuilder> authorContext = contextManager.pop(AuthorBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addAuthor(authorContext.builder().build());
   }

   private static void endContributor(DocumentContextManager contextManager) {
      final HandlerContext<ContributorBuilder> contributorContext = contextManager.pop(ContributorBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addContributor(contributorContext.builder().build());
   }

   private static void endId(DocumentContextManager contextManager) {
      final HandlerContext<IdBuilder> idContext = contextManager.pop(IdBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setId(idContext.builder().build());
   }

   private static void endUpdated(DocumentContextManager contextManager) {
      final HandlerContext<UpdatedBuilder> updatedContext = contextManager.pop(UpdatedBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setUpdated(updatedContext.builder().build());
   }

   private static void endPublished(DocumentContextManager contextManager) {
      final HandlerContext<PublishedBuilder> publishedContext = contextManager.pop(PublishedBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setPublished(publishedContext.builder().build());
   }

   private static void endContent(DocumentContextManager contextManager) {
      final HandlerContext<ContentBuilder> content = contextManager.pop(ContentBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setContent(content.builder().build());
   }

   private static void endCategory(DocumentContextManager contextManager) {
      final HandlerContext<CategoryBuilder> category = contextManager.pop(CategoryBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addCategory(category.builder().build());
   }

   private static void endLink(DocumentContextManager contextManager) {
      final HandlerContext<LinkBuilder> category = contextManager.pop(LinkBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().addLink(category.builder().build());
   }

   private static void endRights(DocumentContextManager contextManager) {
      final HandlerContext<RightsBuilder> textConstructContext = contextManager.pop(RightsBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setRights(textConstructContext.builder().build());
   }

   private static void endSummary(DocumentContextManager contextManager) {
      final HandlerContext<SummaryBuilder> textConstructContext = contextManager.pop(SummaryBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setSummary(textConstructContext.builder().build());
   }

   private static void endTitle(DocumentContextManager contextManager) {
      final HandlerContext<TitleBuilder> textConstructContext = contextManager.pop(TitleBuilder.class);
      contextManager.peek(EntryBuilder.class).builder().setTitle(textConstructContext.builder().build());
   }
}
