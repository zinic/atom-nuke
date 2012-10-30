package org.atomnuke.jaxrs.provider.jx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.datatype.DatatypeConfigurationException;
import net.jps.jx.JxFactory;
import net.jps.jx.JxParsingException;
import net.jps.jx.JxWritingException;
import net.jps.jx.jackson.JacksonJxFactory;

/**
 *
 * @author zinic
 */
@Provider
@Produces({"application/json", "text/json"})
@Consumes({"application/json", "text/json"})
public class JxMessageBodyProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

   private final Map<Class, ReaderWriterPair> translatorMap;
   private final JxFactory jxFactory;

   public JxMessageBodyProvider() {
      try {
         jxFactory = new JacksonJxFactory();
      } catch (DatatypeConfigurationException dce) {
         throw new RuntimeException(dce);
      }

      translatorMap = new HashMap<Class, ReaderWriterPair>();
   }

   private synchronized <T> ReaderWriterPair<T> newTranslator(Class<T> type) {
      final ReaderWriterPair<T> pair = new ReaderWriterPair(jxFactory, type);
      translatorMap.put(type, pair);

      return pair;
   }

   private synchronized <T> ReaderWriterPair<T> getTranslator(Class<T> type) {
      final ReaderWriterPair<T> pair = translatorMap.get(type);

      return pair != null ? pair : newTranslator(type);
   }

   @Override
   public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      try {
         // attempt to map the type - if no errors then its readable
         getTranslator(type);
         return true;
      } catch (Exception ex) {
         return false;
      }
   }

   @Override
   public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
      final ReaderWriterPair pair = getTranslator(type);

      try {
         return pair.jsonReader().read(entityStream);
      } catch (JxParsingException jpe) {
         throw new IOException(jpe.getMessage(), jpe);
      }
   }

   @Override
   public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      try {
         // attempt to map the type - if no errors then its readable
         getTranslator(type);
         return true;
      } catch (Exception ex) {
         return false;
      }
   }

   @Override
   public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return -1;
   }

   @Override
   public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
      final ReaderWriterPair pair = getTranslator(type);

      try {
         pair.jsonWriter().write(t, entityStream);
      } catch (JxWritingException jwe) {
         throw new IOException(jwe.getMessage(), jwe);
      }
   }
}
