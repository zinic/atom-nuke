package org.atomnuke.config.jee6;

import com.oracle.javaee6.ApplicationType;
import com.oracle.javaee6.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.atomnuke.util.config.io.marshall.jaxb.JaxbConfigurationMarhsaller;

/**
 *
 * @author zinic
 */
public class ApplicationXmlMarshaller extends JaxbConfigurationMarhsaller<ApplicationType> {

   private final static QName Application_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "application");

   public ApplicationXmlMarshaller() throws JAXBException {
      this(JAXBContext.newInstance(ObjectFactory.class));
   }

   private ApplicationXmlMarshaller(JAXBContext jaxbc) throws JAXBException {
      this(jaxbc.createMarshaller(), jaxbc.createUnmarshaller());
   }

   private ApplicationXmlMarshaller(Marshaller jaxbMarshaller, Unmarshaller jaxbUnmarshaller) {
      super(ApplicationType.class, Application_QNAME, jaxbMarshaller, jaxbUnmarshaller);
   }
}
