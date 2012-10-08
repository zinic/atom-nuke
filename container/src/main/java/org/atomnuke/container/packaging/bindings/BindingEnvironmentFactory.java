package org.atomnuke.container.packaging.bindings;

import java.util.List;
import org.atomnuke.container.packaging.bindings.environment.BindingEnvironment;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public interface BindingEnvironmentFactory {

   List<BindingEnvironment> newEnviornmentList(ResourceManager resourceManager);
}
