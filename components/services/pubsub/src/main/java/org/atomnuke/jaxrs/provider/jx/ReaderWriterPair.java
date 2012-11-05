package org.atomnuke.jaxrs.provider.jx;

import net.jps.jx.JsonReader;
import net.jps.jx.JsonWriter;
import net.jps.jx.JxFactory;

/**
 *
 * @author zinic
 */
public class ReaderWriterPair<T> {

   private final JsonReader<T> jsonReader;
   private final JsonWriter<T> jsonWriter;
   private final Class<T> type;

   public ReaderWriterPair(JxFactory jxFactory, Class<T> type) {
      this.type = type;

      jsonReader = jxFactory.newReader(type);
      jsonWriter = jxFactory.newWriter(type);
   }

   public JsonReader<T> jsonReader() {
      return jsonReader;
   }

   public JsonWriter<T> jsonWriter() {
      return jsonWriter;
   }

   public Class<T> type() {
      return type;
   }
}
