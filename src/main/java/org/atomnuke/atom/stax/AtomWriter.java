package org.atomnuke.atom.stax;

import org.atomnuke.atom.WriterConfiguration;
import java.net.URI;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Content;
import org.atomnuke.atom.model.DateConstruct;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.PersonConstruct;
import org.atomnuke.atom.model.Published;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Source;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Summary;
import org.atomnuke.atom.model.TextConstruct;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Type;
import org.atomnuke.atom.model.Updated;
import org.atomnuke.atom.xml.AtomNamespaceContext;

/**
 *
 * @author zinic
 */
public final class AtomWriter {

   private static final AtomWriter INSTANCE = new AtomWriter();

   public static AtomWriter instance() {
      return INSTANCE;
   }

   private AtomWriter() {
   }

   private static void writeStartElement(WriterContext context, String ns, String element) throws XMLStreamException {
      if (element == null) {
         return;
      }

      switch (context.getConfiguration().getNamespaceLevel()) {
         case PREFXIED:
            context.getWriter().writeStartElement(ns, element);
            break;

         case NONE:
         default:
            context.getWriter().writeStartElement(element);
      }
   }

   private static void writeEndElement(WriterContext context) throws XMLStreamException {
      context.getWriter().writeEndElement();
   }

   private static void writeAttribute(WriterContext context, String ns, String localName, String value) throws XMLStreamException {
      if (value == null) {
         return;
      }

      switch (context.getConfiguration().getNamespaceLevel()) {
         case PREFXIED:
            context.getWriter().writeAttribute(ns, localName, value);
            break;

         case NONE:
         default:
            context.getWriter().writeAttribute(localName, value);
      }
   }

   private static void writeLang(WriterContext context, String lang) throws XMLStreamException {
      writeAttribute(context, AtomNamespaceContext.XML_NAMESPACE, "lang", lang);
   }

   private static void writeBase(WriterContext context, URI uri) throws XMLStreamException {
      if (uri == null) {
         return;
      }

      writeAttribute(context, AtomNamespaceContext.XML_NAMESPACE, "base", uri.toString());
   }

   private static void writeStringElement(WriterContext context, String element, String value) throws XMLStreamException {
      if (value == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, element);
      context.getWriter().writeCharacters(value);
      writeEndElement(context);
   }

   private static void writeType(WriterContext context, Type type) throws XMLStreamException {
      writeType(context, type != null ? type.toString().toLowerCase() : null);
   }

   private static void writeType(WriterContext context, String type) throws XMLStreamException {
      writeAttribute(context, AtomNamespaceContext.XML_NAMESPACE, "type", type);
   }

   private static void writeSrc(WriterContext context, String src) throws XMLStreamException {
      writeAttribute(context, AtomNamespaceContext.XML_NAMESPACE, "src", src);
   }

   private static void writeUriElement(WriterContext context, String uri) throws XMLStreamException {
      writeStringElement(context, "uri", uri);
   }

   private static void writePersonConstructs(WriterContext context, String element, List<? extends PersonConstruct> people) throws XMLStreamException {
      for (PersonConstruct person : people) {
         writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, element);

         writeLang(context, person.lang());
         writeBase(context, person.base());

         writeStringElement(context, "name", person.name());
         writeStringElement(context, "email", person.email());
         writeUriElement(context, person.uri());

         writeEndElement(context);
      }
   }

   private static void writeAuthors(WriterContext context, List<? extends PersonConstruct> people) throws XMLStreamException {
      writePersonConstructs(context, "author", people);
   }

   private static void writeContributors(WriterContext context, List<? extends PersonConstruct> people) throws XMLStreamException {
      writePersonConstructs(context, "contributor", people);
   }

   private static void writeLinks(WriterContext context, List<Link> links) throws XMLStreamException {
      for (Link link : links) {
         writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "link");

         writeLang(context, link.lang());
         writeBase(context, link.base());

         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "href", link.href());
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "hreflang", link.hreflang());
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "rel", link.rel());
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "title", link.title());
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "type", link.type());

         final Integer length = link.length();
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "length", length == null ? null : length.toString());

         writeEndElement(context);
      }
   }

   private static void writeCategories(WriterContext context, List<Category> categories) throws XMLStreamException {
      for (Category category : categories) {
         writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "category");

         writeLang(context, category.lang());
         writeBase(context, category.base());

         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "label", category.label());
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "scheme", category.scheme());
         writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "term", category.term());

         writeEndElement(context);
      }
   }

   private static void writeGenerator(WriterContext context, Generator generator) throws XMLStreamException {
      if (generator == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "generator");

      writeLang(context, generator.lang());
      writeBase(context, generator.base());

      writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "uri", generator.uri());
      writeAttribute(context, AtomNamespaceContext.ATOM_NAMESPACE, "version", generator.version());

      context.getWriter().writeCharacters(generator.toString());

      writeEndElement(context);
   }

   private static void writeIcon(WriterContext context, Icon icon) throws XMLStreamException {
      if (icon == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "icon");

      writeLang(context, icon.lang());
      writeBase(context, icon.base());

      context.getWriter().writeCharacters(icon.toString());

      writeEndElement(context);
   }

   private static void writeId(WriterContext context, Id id) throws XMLStreamException {
      if (id == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "id");

      writeLang(context, id.lang());
      writeBase(context, id.base());

      context.getWriter().writeCharacters(id.toString());

      writeEndElement(context);
   }

   private static void writeLogo(WriterContext context, Logo logo) throws XMLStreamException {
      if (logo == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "logo");

      writeLang(context, logo.lang());
      writeBase(context, logo.base());

      context.getWriter().writeCharacters(logo.toString());

      writeEndElement(context);
   }

   private static void writeTextConstruct(WriterContext context, String element, TextConstruct text) throws XMLStreamException {
      if (text == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, element);

      writeLang(context, text.lang());
      writeBase(context, text.base());
      writeType(context, text.type());

      context.getWriter().writeCharacters(text.toString());

      writeEndElement(context);
   }

   private static void writeRights(WriterContext context, Rights rights) throws XMLStreamException {
      writeTextConstruct(context, "rights", rights);
   }

   private static void writeTitle(WriterContext context, Title title) throws XMLStreamException {
      writeTextConstruct(context, "title", title);
   }

   private static void writeSubtitle(WriterContext context, Subtitle subtitle) throws XMLStreamException {
      writeTextConstruct(context, "subtitle", subtitle);
   }

   private static void writeSummary(WriterContext context, Summary summary) throws XMLStreamException {
      writeTextConstruct(context, "summary", summary);
   }

   private static void writeDateConstruct(WriterContext context, String element, DateConstruct dateConstruct) throws XMLStreamException {
      if (dateConstruct == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, element);

      writeLang(context, dateConstruct.lang());
      writeBase(context, dateConstruct.base());

      context.getWriter().writeCharacters(dateConstruct.toString());

      writeEndElement(context);
   }

   private static void writeUpdated(WriterContext context, Updated updated) throws XMLStreamException {
      writeDateConstruct(context, "updated", updated);
   }

   private static void writePublished(WriterContext context, Published published) throws XMLStreamException {
      writeDateConstruct(context, "published", published);
   }

   private static void writeSource(WriterContext context, Source source) throws XMLStreamException {
      if (source == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "source");

      writeLang(context, source.lang());
      writeBase(context, source.base());

      writeAuthors(context, source.authors());
      writeLinks(context, source.links());
      writeCategories(context, source.categories());

      writeGenerator(context, source.generator());
      writeIcon(context, source.icon());
      writeId(context, source.id());

      writeLogo(context, source.logo());

      writeRights(context, source.rights());
      writeSubtitle(context, source.subtitle());
      writeTitle(context, source.title());

      writeUpdated(context, source.updated());

      writeEndElement(context);
   }

   private static void writeContent(WriterContext context, Content content) throws XMLStreamException {
      if (content == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "content");

      writeLang(context, content.lang());
      writeBase(context, content.base());
      writeType(context, content.type());
      writeSrc(context, content.src());

      context.getWriter().writeCharacters(content.toString());

      writeEndElement(context);
   }

   private static void writeEntries(WriterContext context, List<Entry> entries) throws XMLStreamException {
      for (Entry entry : entries) {
         writeNonNamespacedEntry(context, entry);
      }
   }

   private static void writeEntry(WriterContext context, Entry entry) throws XMLStreamException {
      if (entry == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "entry");
      bindNamespaces(context);

      writeLang(context, entry.lang());
      writeBase(context, entry.base());

      writeAuthors(context, entry.authors());
      writeContributors(context, entry.contributors());
      writeLinks(context, entry.links());
      writeCategories(context, entry.categories());

      writeId(context, entry.id());

      writeRights(context, entry.rights());
      writeTitle(context, entry.title());

      writePublished(context, entry.published());
      writeUpdated(context, entry.updated());
      writeSummary(context, entry.summary());

      writeSource(context, entry.source());
      writeContent(context, entry.content());

      writeEndElement(context);
   }

   private static void writeNonNamespacedEntry(WriterContext context, Entry entry) throws XMLStreamException {
      if (entry == null) {
         return;
      }

      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "entry");

      writeLang(context, entry.lang());
      writeBase(context, entry.base());

      writeAuthors(context, entry.authors());
      writeContributors(context, entry.contributors());
      writeLinks(context, entry.links());
      writeCategories(context, entry.categories());

      writeId(context, entry.id());

      writeRights(context, entry.rights());
      writeTitle(context, entry.title());

      writePublished(context, entry.published());
      writeUpdated(context, entry.updated());
      writeSummary(context, entry.summary());

      writeSource(context, entry.source());
      writeContent(context, entry.content());

      writeEndElement(context);
   }

   private static void writeFeed(WriterContext context, Feed feed) throws XMLStreamException {
      writeStartElement(context, AtomNamespaceContext.ATOM_NAMESPACE, "feed");
      bindNamespaces(context);

      writeLang(context, feed.lang());
      writeBase(context, feed.base());

      writeAuthors(context, feed.authors());
      writeContributors(context, feed.contributors());
      writeLinks(context, feed.links());
      writeCategories(context, feed.categories());

      writeGenerator(context, feed.generator());
      writeIcon(context, feed.icon());
      writeId(context, feed.id());

      writeLogo(context, feed.logo());

      writeRights(context, feed.rights());
      writeSubtitle(context, feed.subtitle());
      writeTitle(context, feed.title());

      writeUpdated(context, feed.updated());

      writeEntries(context, feed.entries());

      writeEndElement(context);
   }

   private static void bindNamespaces(WriterContext context) throws XMLStreamException {
      final XMLStreamWriter writer = context.getWriter();

      if (context.getConfiguration().getNamespaceLevel() == WriterConfiguration.NamespaceLevel.PREFXIED) {
         writer.writeNamespace(AtomNamespaceContext.ATOM_PREFIX, AtomNamespaceContext.ATOM_NAMESPACE);
         writer.writeNamespace(AtomNamespaceContext.XML_PREFIX, AtomNamespaceContext.XML_NAMESPACE);
      } else {
         writer.writeNamespace("", AtomNamespaceContext.ATOM_NAMESPACE);
      }
   }

   public void write(WriterContext context, Feed f) throws XMLStreamException {
      final XMLStreamWriter writer = context.getWriter();
      writer.setNamespaceContext(AtomNamespaceContext.instance());

      writer.writeStartDocument();
      writeFeed(context, f);
      writer.writeEndDocument();
   }

   public void write(WriterContext context, Entry e) throws XMLStreamException {
      final XMLStreamWriter writer = context.getWriter();
      writer.setNamespaceContext(AtomNamespaceContext.instance());

      writer.writeStartDocument();
      writeEntry(context, e);
      writer.writeEndDocument();
   }
}
