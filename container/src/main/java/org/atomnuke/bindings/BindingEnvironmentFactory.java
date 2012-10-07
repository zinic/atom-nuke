package org.atomnuke.bindings;

import java.util.List;
import org.atomnuke.bindings.context.BindingEnvironment;
import org.atomnuke.container.packaging.resource.ResourceManager;

/**
 *
 * @author zinic
 */
public interface BindingEnvironmentFactory {

   List<BindingEnvironment> newEnviornmentList(ResourceManager resourceManager);
}
