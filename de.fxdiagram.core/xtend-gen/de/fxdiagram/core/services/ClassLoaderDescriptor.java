package de.fxdiagram.core.services;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ClassLoaderProvider;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

@ModelNode({ "classLoaderID", "provider" })
@SuppressWarnings("all")
public class ClassLoaderDescriptor implements DomainObjectDescriptor {
  public ClassLoaderDescriptor(final String classLoaderID, final ClassLoaderProvider provider) {
    this.classLoaderIDProperty.set(classLoaderID);
    this.providerProperty.set(provider);
  }
  
  @Override
  public String getName() {
    return this.getClassLoaderID();
  }
  
  public String toURI(final String resourcePath) {
    ClassLoaderProvider _provider = this.getProvider();
    return _provider.toURI(resourcePath, this);
  }
  
  public Class<?> loadClass(final String className) {
    ClassLoaderProvider _provider = this.getProvider();
    return _provider.loadClass(className, this);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public ClassLoaderDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(classLoaderIDProperty, String.class);
    modelElement.addProperty(providerProperty, ClassLoaderProvider.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private ReadOnlyStringWrapper classLoaderIDProperty = new ReadOnlyStringWrapper(this, "classLoaderID");
  
  public String getClassLoaderID() {
    return this.classLoaderIDProperty.get();
  }
  
  public ReadOnlyStringProperty classLoaderIDProperty() {
    return this.classLoaderIDProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyObjectWrapper<ClassLoaderProvider> providerProperty = new ReadOnlyObjectWrapper<ClassLoaderProvider>(this, "provider");
  
  public ClassLoaderProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public ReadOnlyObjectProperty<ClassLoaderProvider> providerProperty() {
    return this.providerProperty.getReadOnlyProperty();
  }
}
