package org.atomnuke.container.service.resolution;

/**
 *
 * @author zinic
 */
public class DependencyDescriptor {

   public static final DependencyDescriptor[] EMPTY_DESCRIPTOR_ARRAY = new DependencyDescriptor[0];
   public static final String ANY_NAME = "*";

   private final Class requiredServiceInterface;
   private final String requiredServiceName;

   public DependencyDescriptor(Class requiredServiceInterface, String nameQualifier) {
      this.requiredServiceInterface = requiredServiceInterface;
      this.requiredServiceName = nameQualifier;
   }

   public Class requiredServiceInterface() {
      return requiredServiceInterface;
   }

   public String nameQualifier() {
      return requiredServiceName;
   }

   public boolean descriptorMatches(Class serviceInterface, String serviceName) {
      boolean serviceDescribedMatchesGiven = serviceInterface.isAssignableFrom(requiredServiceInterface);

      if (serviceDescribedMatchesGiven) {
         serviceDescribedMatchesGiven = requiredServiceName.equals(ANY_NAME)  || serviceName.equals(ANY_NAME) || serviceName.equals(requiredServiceName);
      }

      return serviceDescribedMatchesGiven;
   }
}
