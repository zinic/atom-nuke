package org.atomnuke.fallout.config.server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.atomnuke.atombus.config.model.ObjectFactory;
import org.atomnuke.atombus.config.model.ServerConfiguration;
import org.atomnuke.util.config.io.marshall.jaxb.JaxbConfigurationMarhsaller;

/**
 *
 * @author zinic
 */
public class ServerConfigurationMarhsaller extends JaxbConfigurationMarhsaller<ServerConfiguration> {

   private static final QName NUKE_QNAME = new QName("http://atomnuke.org/configuration/atombus", "atombus");

   public ServerConfigurationMarhsaller() throws JAXBException {
      this(JAXBContext.newInstance(ObjectFactory.class));
   }

   private ServerConfigurationMarhsaller(JAXBContext jaxbc) throws JAXBException {
      this(jaxbc.createMarshaller(), jaxbc.createUnmarshaller());
   }

   private ServerConfigurationMarhsaller(Marshaller jaxbMarshaller, Unmarshaller jaxbUnmarshaller) {
      super(ServerConfiguration.class, NUKE_QNAME, jaxbMarshaller, jaxbUnmarshaller);
   }
}
