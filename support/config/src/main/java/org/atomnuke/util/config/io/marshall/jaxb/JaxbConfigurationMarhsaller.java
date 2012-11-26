package org.atomnuke.util.config.io.marshall.jaxb;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public class JaxbConfigurationMarhsaller<T> implements ConfigurationMarshaller<T> {

   public static <T> ConfigurationMarshaller<T> newJaxConfigurationMarshaller(Class<T> rootType, QName rootQName) throws JAXBException {
      final JAXBContext jaxbc = JAXBContext.newInstance(rootType.getPackage().getName());

      return new JaxbConfigurationMarhsaller(rootType, rootQName, jaxbc.createMarshaller(), jaxbc.createUnmarshaller());
   }

   private final Unmarshaller jaxbUnmarshaller;
   private final Marshaller jaxbMarshaller;
   private final Class<T> rootType;
   private final QName rootQName;

   public JaxbConfigurationMarhsaller(Class<T> rootType, QName rootQName, Marshaller jaxbMarshaller, Unmarshaller jaxbUnmarshaller) {
      this.rootType = rootType;
      this.rootQName = rootQName;
      this.jaxbMarshaller = jaxbMarshaller;
      this.jaxbUnmarshaller = jaxbUnmarshaller;
   }

   @Override
   public void marshall(T obj, OutputStream out) throws ConfigurationException {
      try {
         jaxbMarshaller.marshal(new JAXBElement<T>(rootQName, rootType, obj), out);
      } catch (JAXBException jaxbe) {
         throw new ConfigurationException("Failed to marshall configuration. Reason: " + jaxbe.getMessage(), jaxbe);
      }
   }

   @Override
   public T unmarhsall(InputStream in) throws ConfigurationException {
      try {
         final Object o = jaxbUnmarshaller.unmarshal(in);

         return (T) (o instanceof JAXBElement ? ((JAXBElement)o).getValue() : o);
      } catch (JAXBException jaxbe) {
         throw new ConfigurationException("Failed to unmarshall configuration. Reason: " + jaxbe.getMessage(), jaxbe);
      }
   }
}
