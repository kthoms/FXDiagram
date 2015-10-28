package de.fxdiagram.examples.java;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.CachedDomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import org.eclipse.xtext.xbase.lib.Exceptions;

@ModelNode
@SuppressWarnings("all")
public class JavaSuperTypeDescriptor extends CachedDomainObjectDescriptor<JavaSuperTypeHandle> {
  public JavaSuperTypeDescriptor(final JavaSuperTypeHandle it, final JavaModelProvider provider) {
    super(it, ((it.getSubType().getCanonicalName() + "->") + it.getSuperType().getCanonicalName()), provider);
  }
  
  @Override
  public JavaSuperTypeHandle resolveDomainObject() {
    try {
      JavaSuperTypeHandle _xblockexpression = null;
      {
        String _id = this.getId();
        final String[] split = _id.split("->");
        String _get = split[0];
        Class<?> _forName = Class.forName(_get);
        String _get_1 = split[1];
        Class<?> _forName_1 = Class.forName(_get_1);
        _xblockexpression = new JavaSuperTypeHandle(_forName, _forName_1);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public JavaSuperTypeDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
