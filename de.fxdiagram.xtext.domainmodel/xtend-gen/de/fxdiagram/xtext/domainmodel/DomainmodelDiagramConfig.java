package de.fxdiagram.xtext.domainmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.xtext.ESetting;
import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.shapes.BaseDiagramNode;
import de.fxdiagram.xtext.domainmodel.DomainModelUtil;
import de.fxdiagram.xtext.domainmodel.EntityNode;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.example.domainmodel.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.domainmodel.DomainmodelPackage;
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.domainmodel.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DomainmodelDiagramConfig extends AbstractXtextDiagramConfig {
  @Inject
  @Extension
  private DomainModelUtil domainModelUtil;
  
  private final DiagramMapping<PackageDeclaration> packageDiagram = new DiagramMapping<PackageDeclaration>(this, "packageDiagram", "Package diagram") {
    @Override
    public void calls() {
      final Function1<PackageDeclaration, Iterable<? extends Entity>> _function = (PackageDeclaration it) -> {
        EList<AbstractElement> _elements = it.getElements();
        return Iterables.<Entity>filter(_elements, Entity.class);
      };
      this.<Entity>nodeForEach(DomainmodelDiagramConfig.this.entityNode, _function);
      final Function1<PackageDeclaration, Iterable<? extends PackageDeclaration>> _function_1 = (PackageDeclaration it) -> {
        EList<AbstractElement> _elements = it.getElements();
        return Iterables.<PackageDeclaration>filter(_elements, PackageDeclaration.class);
      };
      this.<PackageDeclaration>nodeForEach(DomainmodelDiagramConfig.this.packageNode, _function_1);
      this.eagerly(DomainmodelDiagramConfig.this.superTypeConnection, DomainmodelDiagramConfig.this.propertyConnection);
    }
  };
  
  private final NodeMapping<PackageDeclaration> packageNode = new NodeMapping<PackageDeclaration>(this, "packageNode", "Package node") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<PackageDeclaration> descriptor) {
      return new BaseDiagramNode<PackageDeclaration>(descriptor);
    }
    
    @Override
    public void calls() {
      final Function1<PackageDeclaration, PackageDeclaration> _function = (PackageDeclaration it) -> {
        return it;
      };
      this.<PackageDeclaration>nestedDiagramFor(DomainmodelDiagramConfig.this.packageDiagram, _function);
    }
  };
  
  private final NodeMapping<Entity> entityNode = new NodeMapping<Entity>(this, "entityNode", "Entity") {
    @Override
    public XNode createNode(final IMappedElementDescriptor<Entity> descriptor) {
      return new EntityNode(descriptor);
    }
    
    @Override
    public void calls() {
      final Function1<Entity, Iterable<? extends Property>> _function = (Entity it) -> {
        EList<Feature> _features = it.getFeatures();
        Iterable<Property> _filter = Iterables.<Property>filter(_features, Property.class);
        final Function1<Property, Boolean> _function_1 = (Property it_1) -> {
          JvmTypeReference _type = it_1.getType();
          Entity _referencedEntity = DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(_type);
          return Boolean.valueOf((!Objects.equal(_referencedEntity, null)));
        };
        return IterableExtensions.<Property>filter(_filter, _function_1);
      };
      MultiConnectionMappingCall<Property, Entity> _outConnectionForEach = this.<Property>outConnectionForEach(DomainmodelDiagramConfig.this.propertyConnection, _function);
      final Function1<Side, Node> _function_1 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add property");
      };
      _outConnectionForEach.asButton(_function_1);
      final Function1<Entity, Iterable<? extends ESetting<Entity>>> _function_2 = (Entity entity) -> {
        List<? extends ESetting<Entity>> _xblockexpression = null;
        {
          JvmParameterizedTypeReference _superType = entity.getSuperType();
          final Entity superEntity = DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(_superType);
          List<? extends ESetting<Entity>> _xifexpression = null;
          boolean _equals = Objects.equal(superEntity, null);
          if (_equals) {
            _xifexpression = CollectionLiterals.emptyList();
          } else {
            ESetting<Entity> _eSetting = new ESetting<Entity>(entity, DomainmodelPackage.Literals.ENTITY__SUPER_TYPE, 0);
            _xifexpression = Collections.<ESetting<Entity>>unmodifiableList(CollectionLiterals.<ESetting<Entity>>newArrayList(_eSetting));
          }
          _xblockexpression = _xifexpression;
        }
        return _xblockexpression;
      };
      MultiConnectionMappingCall<ESetting<Entity>, Entity> _outConnectionForEach_1 = this.<ESetting<Entity>>outConnectionForEach(DomainmodelDiagramConfig.this.superTypeConnection, _function_2);
      final Function1<Side, Node> _function_3 = (Side it) -> {
        return ButtonExtensions.getTriangleButton(it, "Add superclass");
      };
      _outConnectionForEach_1.asButton(_function_3);
    }
  };
  
  private final ConnectionMapping<Property> propertyConnection = new ConnectionMapping<Property>(this, "propertyConnection", "Property") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<Property> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = (XConnection it) -> {
        LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
        it.setTargetArrowHead(_lineArrowHead);
        XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
        final Procedure1<XConnectionLabel> _function_1 = (XConnectionLabel label) -> {
          Text _text = label.getText();
          final Function1<Property, String> _function_2 = (Property it_1) -> {
            return it_1.getName();
          };
          String _withDomainObject = descriptor.<String>withDomainObject(_function_2);
          _text.setText(_withDomainObject);
        };
        ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function_1);
      };
      return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<Property, Entity> _function = (Property it) -> {
        JvmTypeReference _type = it.getType();
        return DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(_type);
      };
      this.<Entity>target(DomainmodelDiagramConfig.this.entityNode, _function);
    }
  };
  
  private final ConnectionMapping<ESetting<Entity>> superTypeConnection = new ConnectionMapping<ESetting<Entity>>(this, "superTypeConnection", "Supertype") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<ESetting<Entity>> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = (XConnection it) -> {
        TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, 
          null, Color.WHITE, false);
        it.setTargetArrowHead(_triangleArrowHead);
      };
      return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<ESetting<Entity>, Entity> _function = (ESetting<Entity> it) -> {
        Object _target = it.getTarget();
        return DomainmodelDiagramConfig.this.domainModelUtil.getReferencedEntity(((JvmTypeReference) _target));
      };
      this.<Entity>target(DomainmodelDiagramConfig.this.entityNode, _function);
    }
  };
  
  @Override
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (!_matched) {
      if (domainArgument instanceof Entity) {
        _matched=true;
        acceptor.add(this.entityNode);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof PackageDeclaration) {
        _matched=true;
        acceptor.add(this.packageNode);
        acceptor.add(this.packageDiagram);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof Property) {
        _matched=true;
        acceptor.add(this.propertyConnection);
      }
    }
  }
}
