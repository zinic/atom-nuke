package net.jps.nuke.atom.stax;

import java.net.URI;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Content;
import net.jps.nuke.atom.model.DateConstruct;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.ID;
import net.jps.nuke.atom.model.Icon;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.Logo;
import net.jps.nuke.atom.model.PersonConstruct;
import net.jps.nuke.atom.model.Published;
import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Source;
import net.jps.nuke.atom.model.Subtitle;
import net.jps.nuke.atom.model.Summary;
import net.jps.nuke.atom.model.TextConstruct;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Type;
import net.jps.nuke.atom.model.Updated;
import net.jps.nuke.atom.sax.handler.AtomHandler;
import net.jps.nuke.atom.xml.AtomNamespaceContext;

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

   private static void writeAttribute(XMLStreamWriter output, String ns, String localName, String value) throws XMLStreamException {
      if (value != null) {
         output.writeAttribute(ns, localName, value);
      }
   }

   private static void writeLang(XMLStreamWriter output, String lang) throws XMLStreamException {
      writeAttribute(output, AtomNamespaceContext.XML_NAMESPACE, "lang", lang);
   }

   private static void writeBase(XMLStreamWriter output, URI uri) throws XMLStreamException {
      if (uri != null) {
         writeAttribute(output, AtomNamespaceContext.XML_NAMESPACE, "base", uri.toString());
      }
   }

   private static void writeStringElement(XMLStreamWriter output, String element, String value) throws XMLStreamException {
      if (value != null) {
         output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, element);
         output.writeCharacters(value);
         output.writeEndElement();
      }
   }

   private static void writeType(XMLStreamWriter output, Type type) throws XMLStreamException {
      writeType(output, type != null ? type.toString().toLowerCase() : null);
   }

   private static void writeType(XMLStreamWriter output, String type) throws XMLStreamException {
      writeAttribute(output, AtomNamespaceContext.XML_NAMESPACE, "type", type);
   }

   private static void writeUriElement(XMLStreamWriter output, String uri) throws XMLStreamException {
      writeStringElement(output, "uri", uri);
   }

   private static void writePersonConstructs(XMLStreamWriter output, String element, List<? extends PersonConstruct> people) throws XMLStreamException {
      for (PersonConstruct person : people) {
         output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, element);

         writeLang(output, person.lang());
         writeBase(output, person.base());

         writeStringElement(output, "name", person.name());
         writeStringElement(output, "email", person.email());
         writeUriElement(output, element);

         output.writeEndElement();
      }
   }

   private static void writeAuthors(XMLStreamWriter output, List<? extends PersonConstruct> people) throws XMLStreamException {
      writePersonConstructs(output, "author", people);
   }

   private static void writeContributors(XMLStreamWriter output, List<? extends PersonConstruct> people) throws XMLStreamException {
      writePersonConstructs(output, "contributor", people);
   }

   private static void writeLinks(XMLStreamWriter output, List<Link> links) throws XMLStreamException {
      for (Link link : links) {
         output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "link");

         writeLang(output, link.lang());
         writeBase(output, link.base());

         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "href", link.href());
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "hreflang", link.hreflang());
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "rel", link.rel());
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "title", link.title());
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "type", link.type());
         
         final Integer length = link.length();
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "length", length == null ? null : length.toString());

         output.writeEndElement();
      }
   }

   private static void writeCategories(XMLStreamWriter output, List<Category> categories) throws XMLStreamException {
      for (Category category : categories) {
         output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "category");

         writeLang(output, category.lang());
         writeBase(output, category.base());

         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "label", category.label());
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "scheme", category.scheme());
         writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "term", category.term());

         output.writeEndElement();
      }
   }

   private static void writeGenerator(XMLStreamWriter output, Generator generator) throws XMLStreamException {
      if (generator == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "generator");

      writeLang(output, generator.lang());
      writeBase(output, generator.base());

      writeUriElement(output, generator.uri());
      writeAttribute(output, AtomNamespaceContext.ATOM_NAMESPACE, "version", generator.version());

      output.writeCharacters(generator.value());

      output.writeEndElement();
   }

   private static void writeIcon(XMLStreamWriter output, Icon icon) throws XMLStreamException {
      if (icon == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "icon");

      writeLang(output, icon.lang());
      writeBase(output, icon.base());

      output.writeCharacters(icon.value());

      output.writeEndElement();
   }

   private static void writeId(XMLStreamWriter output, ID id) throws XMLStreamException {
      if (id == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "id");

      writeLang(output, id.lang());
      writeBase(output, id.base());

      output.writeCharacters(id.value());

      output.writeEndElement();
   }

   private static void writeLogo(XMLStreamWriter output, Logo logo) throws XMLStreamException {
      if (logo == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "logo");

      writeLang(output, logo.lang());
      writeBase(output, logo.base());

      output.writeCharacters(logo.value());

      output.writeEndElement();
   }

   private static void writeTextConstruct(XMLStreamWriter output, String element, TextConstruct text) throws XMLStreamException {
      if (text == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, element);

      writeLang(output, text.lang());
      writeBase(output, text.base());
      writeType(output, text.type());

      output.writeCharacters(text.value());

      output.writeEndElement();
   }

   private static void writeRights(XMLStreamWriter output, Rights rights) throws XMLStreamException {
      writeTextConstruct(output, "rights", rights);
   }

   private static void writeTitle(XMLStreamWriter output, Title title) throws XMLStreamException {
      writeTextConstruct(output, "title", title);
   }

   private static void writeSubtitle(XMLStreamWriter output, Subtitle subtitle) throws XMLStreamException {
      writeTextConstruct(output, "subtitle", subtitle);
   }

   private static void writeSummary(XMLStreamWriter output, Summary summary) throws XMLStreamException {
      writeTextConstruct(output, "summary", summary);
   }

   private static void writeDateConstruct(XMLStreamWriter output, String element, DateConstruct dateConstruct) throws XMLStreamException {
      if (dateConstruct == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, element);

      writeLang(output, dateConstruct.lang());
      writeBase(output, dateConstruct.base());

      output.writeCharacters(dateConstruct.asText());

      output.writeEndElement();
   }

   private static void writeUpdated(XMLStreamWriter output, Updated updated) throws XMLStreamException {
      writeDateConstruct(output, "updated", updated);
   }

   private static void writePublished(XMLStreamWriter output, Published published) throws XMLStreamException {
      writeDateConstruct(output, "published", published);
   }

   private static void writeSource(XMLStreamWriter output, Source source) throws XMLStreamException {
      if (source == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "source");

      writeLang(output, source.lang());
      writeBase(output, source.base());

      writeAuthors(output, source.authors());
      writeLinks(output, source.links());
      writeCategories(output, source.categories());

      writeGenerator(output, source.generator());
      writeIcon(output, source.icon());
      writeId(output, source.id());

      writeLogo(output, source.logo());

      writeRights(output, source.rights());
      writeSubtitle(output, source.subtitle());
      writeTitle(output, source.title());

      writeUpdated(output, source.updated());

      output.writeEndElement();
   }

   private static void writeContent(XMLStreamWriter output, Content content) throws XMLStreamException {
      if (content == null) {
         return;
      }

      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "content");

      writeLang(output, content.lang());
      writeBase(output, content.base());
      writeType(output, content.type());

      output.writeCharacters(content.value());

      output.writeEndElement();
   }

   private static void writeEntries(XMLStreamWriter output, List<Entry> entries) throws XMLStreamException {
      for (Entry entry : entries) {
         writeEntry(output, entry);
      }
   }

   private static void writeEntry(XMLStreamWriter output, Entry entry) throws XMLStreamException {
      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "entry");
      bindNamespaces(output);

      writeLang(output, entry.lang());
      writeBase(output, entry.base());

      writeAuthors(output, entry.authors());
      writeContributors(output, entry.contributors());
      writeLinks(output, entry.links());
      writeCategories(output, entry.categories());

      writeId(output, entry.id());

      writeRights(output, entry.rights());
      writeTitle(output, entry.title());

      writePublished(output, entry.published());
      writeUpdated(output, entry.updated());
      writeSummary(output, entry.summary());

      writeSource(output, entry.source());
      writeContent(output, entry.content());

      output.writeEndElement();
   }

   private static void writeFeed(XMLStreamWriter output, Feed feed) throws XMLStreamException {
      output.writeStartElement(AtomNamespaceContext.ATOM_NAMESPACE, "feed");
      bindNamespaces(output);

      writeLang(output, feed.lang());
      writeBase(output, feed.base());

      writeAuthors(output, feed.authors());
      writeContributors(output, feed.contributors());
      writeLinks(output, feed.links());
      writeCategories(output, feed.categories());

      writeGenerator(output, feed.generator());
      writeIcon(output, feed.icon());
      writeId(output, feed.id());

      writeLogo(output, feed.logo());

      writeRights(output, feed.rights());
      writeSubtitle(output, feed.subtitle());
      writeTitle(output, feed.title());

      writeUpdated(output, feed.updated());

      writeEntries(output, feed.entries());

      output.writeEndElement();
   }

   private static void bindNamespaces(XMLStreamWriter output) throws XMLStreamException {
      output.writeNamespace(AtomNamespaceContext.ATOM_PREFIX, AtomNamespaceContext.ATOM_NAMESPACE);
      output.writeNamespace(AtomNamespaceContext.XML_PREFIX, AtomNamespaceContext.XML_NAMESPACE);
   }

   public void write(XMLStreamWriter output, Feed f) throws XMLStreamException {
      output.setNamespaceContext(AtomNamespaceContext.instance());
      
      output.writeStartDocument();
      writeFeed(output, f);
      output.writeEndDocument();
   }

   public void write(XMLStreamWriter output, Entry e) throws XMLStreamException {
      output.setNamespaceContext(AtomNamespaceContext.instance());

      output.writeStartDocument();
      writeEntry(output, e);
      output.writeEndDocument();
   }
}
