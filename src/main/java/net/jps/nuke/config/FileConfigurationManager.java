package net.jps.nuke.config;

import java.io.*;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.jps.nuke.config.model.ObjectFactory;
import net.jps.nuke.config.model.ServerConfiguration;

/**
 *
 * @author zinic
 */
public class FileConfigurationManager implements ConfigurationManager {

   private final File configurationDirectory, configurationFile;
    private final ObjectFactory objectFactory;
    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;

    public FileConfigurationManager(Marshaller marshaller, Unmarshaller unmarshaller, ObjectFactory objectFactory, File configurationDirectory, String cfgName) {
        this.configurationDirectory = configurationDirectory;
        this.configurationFile = new File(configurationDirectory, cfgName);

        this.objectFactory = objectFactory;
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }

    @Override
    public synchronized ConfigurationHandler readConfiguration() throws ConfigurationException {
        return new ConfigurationHandler(this, readConfigurationFile());
    }

    private ServerConfiguration readConfigurationFile() throws ConfigurationException {
        if (!configurationDirectory.exists() && !configurationDirectory.mkdir()) {
            throw new ConfigurationException("Unable to make configuration directory: " + configurationDirectory.getAbsolutePath());
        }
        
        if (!configurationFile.exists() || configurationFile.length() == 0) {
            return new ServerConfiguration();
        }
        
        try {
            final InputStream fin = new FileInputStream(configurationFile);
            final ServerConfiguration newConfiguration = read(fin);
            
            fin.close();
            
            return newConfiguration;
        } catch (IOException ioe) {
            throw new ConfigurationException(ioe.getMessage(), ioe);
        }
    }

    private ServerConfiguration read(InputStream in) throws ConfigurationException {
        try {
            final Object o = unmarshaller.unmarshal(in);
            return o instanceof JAXBElement ? ((JAXBElement<ServerConfiguration>) o).getValue() : (ServerConfiguration) o;
        } catch (JAXBException jaxbe) {
            throw new ConfigurationException(jaxbe.getLinkedException().getMessage(), jaxbe);
        }
    }

    @Override
    public synchronized void write(ServerConfiguration configuration) throws ConfigurationException {
        try {
            final FileOutputStream fout = new FileOutputStream(configurationFile);
            write(configuration, fout);
            
            fout.close();
        } catch (IOException ioe) {
            throw new ConfigurationException(ioe.getMessage(), ioe);
        }
    }
    
    private void write(ServerConfiguration configuration, OutputStream out) throws ConfigurationException {
        try {
            marshaller.marshal(objectFactory.createNuke(configuration), out);
        } catch(JAXBException jaxbe) {
            throw new ConfigurationException(jaxbe.getLinkedException().getMessage(), jaxbe);
        }
    }
}
