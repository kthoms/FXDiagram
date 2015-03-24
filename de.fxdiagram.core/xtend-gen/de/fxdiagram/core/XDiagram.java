package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DiagramNavigationBehavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.extensions.InitializingMapListener;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.core.viewport.ViewportTransform;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * A diagram with {@link XNode}s and {@link XConnection}s.
 * 
 * Being added to the {@link #nodes} or {@link #connections}, {@link XShapes} will get
 * automatically be activated.
 * 
 * To find the {@link XDiagram} of an {@link XShape}, use the {@link CoreExtensions}.
 * 
 * Diagrams can be nested ({@link parentDiagram}).
 * An optional {@link contentsInitializer} allows lazy population of the diagram.
 * Just like an {@link XShape}, an {@link XDiagram} can be customized with composable
 * {@link Behavior}s.
 * 
 * A {@link viewportTransform} stores the current viewport of the diagram.
 */
@ModelNode({ "nodes", "connections", "parentDiagram" })
@SuppressWarnings("all")
public class XDiagram extends Group implements XActivatable, XModelProvider {
  private Group nodeLayer = new Group();
  
  private Group buttonLayer = new Group();
  
  private Procedure1<? super XDiagram> contentsInitializer;
  
  private AuxiliaryLinesSupport auxiliaryLinesSupport;
  
  private ObservableMap<Class<? extends Behavior>, Behavior> behaviors = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
  
  private ViewportTransform viewportTransform;
  
  private boolean needsCentering = true;
  
  public XDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    this.isRootDiagramProperty.set(true);
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function = (ObservableValue<? extends Parent> property, Parent oldValue, Parent newValue) -> {
      XDiagram _diagram = CoreExtensions.getDiagram(newValue);
      this.parentDiagramProperty.set(_diagram);
      XDiagram _parentDiagram = this.getParentDiagram();
      boolean _equals = Objects.equal(_parentDiagram, null);
      this.isRootDiagramProperty.set(_equals);
    };
    _parentProperty.addListener(_function);
    final ChangeListener<Boolean> _function_1 = (ObservableValue<? extends Boolean> property, Boolean oldValue, Boolean newValue) -> {
      if ((newValue).booleanValue()) {
        ObservableList<Node> _children_2 = this.nodeLayer.getChildren();
        ObservableList<XConnection> _connections = this.getConnections();
        Iterables.<Node>addAll(_children_2, _connections);
      } else {
        ObservableList<Node> _children_3 = this.nodeLayer.getChildren();
        ObservableList<XConnection> _connections_1 = this.getConnections();
        Iterables.removeAll(_children_3, _connections_1);
      }
    };
    this.isRootDiagramProperty.addListener(_function_1);
    ViewportTransform _viewportTransform = new ViewportTransform();
    this.viewportTransform = _viewportTransform;
    ObservableList<Transform> _transforms = this.getTransforms();
    Transform _transform = this.viewportTransform.getTransform();
    _transforms.setAll(_transform);
    ObservableList<Transform> _transforms_1 = this.getTransforms();
    final ListChangeListener<Transform> _function_2 = (ListChangeListener.Change<? extends Transform> change) -> {
      throw new IllegalStateException("Illegal attempt to change the transforms of an XDiagram");
    };
    _transforms_1.addListener(_function_2);
  }
  
  public ViewportTransform getViewportTransform() {
    return this.viewportTransform;
  }
  
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.isActiveProperty.set(true);
      this.doActivate();
    }
  }
  
  public void doActivate() {
    ObservableList<XNode> _nodes = this.getNodes();
    InitializingListListener<XNode> _initializingListListener = new InitializingListListener<XNode>();
    final Procedure1<InitializingListListener<XNode>> _function = (InitializingListListener<XNode> it) -> {
      final Procedure1<XNode> _function_1 = (XNode it_1) -> {
        it_1.initializeGraphics();
        Group _nodeLayer = this.getNodeLayer();
        ObservableList<Node> _children = _nodeLayer.getChildren();
        _children.add(it_1);
        boolean _isActive = this.getIsActive();
        if (_isActive) {
          it_1.activate();
        }
      };
      it.setAdd(_function_1);
      final Procedure1<XNode> _function_2 = (XNode it_1) -> {
        XDiagram _diagram = CoreExtensions.getDiagram(it_1);
        Group _nodeLayer = _diagram.getNodeLayer();
        ObservableList<Node> _children = _nodeLayer.getChildren();
        _children.remove(it_1);
      };
      it.setRemove(_function_2);
    };
    InitializingListListener<XNode> _doubleArrow = ObjectExtensions.<InitializingListListener<XNode>>operator_doubleArrow(_initializingListListener, _function);
    CoreExtensions.<XNode>addInitializingListener(_nodes, _doubleArrow);
    InitializingListener<ArrowHead> _initializingListener = new InitializingListener<ArrowHead>();
    final Procedure1<InitializingListener<ArrowHead>> _function_1 = (InitializingListener<ArrowHead> it) -> {
      final Procedure1<ArrowHead> _function_2 = (ArrowHead it_1) -> {
        Group _connectionLayer = this.getConnectionLayer();
        ObservableList<Node> _children = _connectionLayer.getChildren();
        boolean _contains = _children.contains(it_1);
        boolean _not = (!_contains);
        if (_not) {
          it_1.initializeGraphics();
          Group _connectionLayer_1 = this.getConnectionLayer();
          ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
          _children_1.add(it_1);
        }
      };
      it.setSet(_function_2);
      final Procedure1<ArrowHead> _function_3 = (ArrowHead it_1) -> {
        Group _connectionLayer = this.getConnectionLayer();
        ObservableList<Node> _children = _connectionLayer.getChildren();
        _children.remove(it_1);
      };
      it.setUnset(_function_3);
    };
    final InitializingListener<ArrowHead> arrowHeadListener = ObjectExtensions.<InitializingListener<ArrowHead>>operator_doubleArrow(_initializingListener, _function_1);
    InitializingListListener<XConnectionLabel> _initializingListListener_1 = new InitializingListListener<XConnectionLabel>();
    final Procedure1<InitializingListListener<XConnectionLabel>> _function_2 = (InitializingListListener<XConnectionLabel> it) -> {
      final Procedure1<XConnectionLabel> _function_3 = (XConnectionLabel it_1) -> {
        Group _connectionLayer = this.getConnectionLayer();
        ObservableList<Node> _children = _connectionLayer.getChildren();
        boolean _contains = _children.contains(it_1);
        boolean _not = (!_contains);
        if (_not) {
          Group _connectionLayer_1 = this.getConnectionLayer();
          ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
          _children_1.add(it_1);
        }
      };
      it.setAdd(_function_3);
      final Procedure1<XConnectionLabel> _function_4 = (XConnectionLabel it_1) -> {
        Group _connectionLayer = this.getConnectionLayer();
        ObservableList<Node> _children = _connectionLayer.getChildren();
        _children.remove(it_1);
      };
      it.setRemove(_function_4);
    };
    final InitializingListListener<XConnectionLabel> labelListener = ObjectExtensions.<InitializingListListener<XConnectionLabel>>operator_doubleArrow(_initializingListListener_1, _function_2);
    ObservableList<XConnection> _connections = this.getConnections();
    InitializingListListener<XConnection> _initializingListListener_2 = new InitializingListListener<XConnection>();
    final Procedure1<InitializingListListener<XConnection>> _function_3 = (InitializingListListener<XConnection> it) -> {
      final Procedure1<XConnection> _function_4 = (XConnection it_1) -> {
        it_1.initializeGraphics();
        Group _connectionLayer = this.getConnectionLayer();
        ObservableList<Node> _children = _connectionLayer.getChildren();
        _children.add(it_1);
        boolean _isActive = this.getIsActive();
        if (_isActive) {
          it_1.activate();
        }
        ListProperty<XConnectionLabel> _labelsProperty = it_1.labelsProperty();
        CoreExtensions.<XConnectionLabel>addInitializingListener(_labelsProperty, labelListener);
        ObjectProperty<ArrowHead> _sourceArrowHeadProperty = it_1.sourceArrowHeadProperty();
        CoreExtensions.<ArrowHead>addInitializingListener(_sourceArrowHeadProperty, arrowHeadListener);
        ObjectProperty<ArrowHead> _targetArrowHeadProperty = it_1.targetArrowHeadProperty();
        CoreExtensions.<ArrowHead>addInitializingListener(_targetArrowHeadProperty, arrowHeadListener);
      };
      it.setAdd(_function_4);
      final Procedure1<XConnection> _function_5 = (XConnection it_1) -> {
        XDiagram _diagram = CoreExtensions.getDiagram(it_1);
        Group _connectionLayer = _diagram.getConnectionLayer();
        ObservableList<Node> _children = _connectionLayer.getChildren();
        _children.remove(it_1);
        ListProperty<XConnectionLabel> _labelsProperty = it_1.labelsProperty();
        CoreExtensions.<XConnectionLabel>removeInitializingListener(_labelsProperty, labelListener);
        ObjectProperty<ArrowHead> _sourceArrowHeadProperty = it_1.sourceArrowHeadProperty();
        CoreExtensions.<ArrowHead>removeInitializingListener(_sourceArrowHeadProperty, arrowHeadListener);
        ObjectProperty<ArrowHead> _targetArrowHeadProperty = it_1.targetArrowHeadProperty();
        CoreExtensions.<ArrowHead>removeInitializingListener(_targetArrowHeadProperty, arrowHeadListener);
      };
      it.setRemove(_function_5);
    };
    InitializingListListener<XConnection> _doubleArrow_1 = ObjectExtensions.<InitializingListListener<XConnection>>operator_doubleArrow(_initializingListListener_2, _function_3);
    CoreExtensions.<XConnection>addInitializingListener(_connections, _doubleArrow_1);
    AuxiliaryLinesSupport _auxiliaryLinesSupport = new AuxiliaryLinesSupport(this);
    this.auxiliaryLinesSupport = _auxiliaryLinesSupport;
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
    NavigationBehavior _behavior = this.<NavigationBehavior>getBehavior(NavigationBehavior.class);
    boolean _equals = Objects.equal(_behavior, null);
    if (_equals) {
      DiagramNavigationBehavior _diagramNavigationBehavior = new DiagramNavigationBehavior(this);
      this.addBehavior(_diagramNavigationBehavior);
    }
    InitializingMapListener<Class<? extends Behavior>, Behavior> _initializingMapListener = new InitializingMapListener<Class<? extends Behavior>, Behavior>();
    final Procedure1<InitializingMapListener<Class<? extends Behavior>, Behavior>> _function_4 = (InitializingMapListener<Class<? extends Behavior>, Behavior> it) -> {
      final Procedure2<Class<? extends Behavior>, Behavior> _function_5 = (Class<? extends Behavior> key, Behavior value) -> {
        value.activate();
      };
      it.setPut(_function_5);
    };
    InitializingMapListener<Class<? extends Behavior>, Behavior> _doubleArrow_2 = ObjectExtensions.<InitializingMapListener<Class<? extends Behavior>, Behavior>>operator_doubleArrow(_initializingMapListener, _function_4);
    CoreExtensions.<Class<? extends Behavior>, Behavior>addInitializingListener(this.behaviors, _doubleArrow_2);
    boolean _isLayoutOnActivate = this.getIsLayoutOnActivate();
    if (_isLayoutOnActivate) {
      this.setIsLayoutOnActivate(false);
      XRoot _root = CoreExtensions.getRoot(this);
      CommandStack _commandStack = _root.getCommandStack();
      Layouter _layouter = new Layouter();
      Duration _millis = DurationExtensions.millis(250);
      LazyCommand _createLayoutCommand = _layouter.createLayoutCommand(LayoutType.DOT, this, _millis);
      _commandStack.execute(_createLayoutCommand);
    }
  }
  
  public void centerDiagram(final boolean useForce) {
    boolean _or = false;
    if (this.needsCentering) {
      _or = true;
    } else {
      _or = useForce;
    }
    if (_or) {
      this.layout();
      Bounds _layoutBounds = this.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = this.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      double _multiply = (_width * _height);
      boolean _greaterThan = (_multiply > 1);
      if (_greaterThan) {
        Scene _scene = this.getScene();
        double _width_1 = _scene.getWidth();
        Bounds _layoutBounds_2 = this.getLayoutBounds();
        double _width_2 = _layoutBounds_2.getWidth();
        double _divide = (_width_1 / _width_2);
        Scene _scene_1 = this.getScene();
        double _height_1 = _scene_1.getHeight();
        Bounds _layoutBounds_3 = this.getLayoutBounds();
        double _height_2 = _layoutBounds_3.getHeight();
        double _divide_1 = (_height_1 / _height_2);
        double _min = Math.min(_divide, _divide_1);
        final double scale = Math.min(1, _min);
        this.viewportTransform.setScale(scale);
        Bounds _boundsInLocal = this.getBoundsInLocal();
        Bounds _localToScene = this.localToScene(_boundsInLocal);
        final Point2D centerInScene = BoundsExtensions.center(_localToScene);
        Scene _scene_2 = this.getScene();
        double _width_3 = _scene_2.getWidth();
        double _multiply_1 = (0.5 * _width_3);
        double _x = centerInScene.getX();
        double _minus = (_multiply_1 - _x);
        Scene _scene_3 = this.getScene();
        double _height_3 = _scene_3.getHeight();
        double _multiply_2 = (0.5 * _height_3);
        double _y = centerInScene.getY();
        double _minus_1 = (_multiply_2 - _y);
        this.viewportTransform.setTranslate(_minus, _minus_1);
      }
    }
    this.needsCentering = false;
  }
  
  public <T extends Behavior> T getBehavior(final Class<T> key) {
    Behavior _get = this.behaviors.get(key);
    return ((T) _get);
  }
  
  public Behavior addBehavior(final Behavior behavior) {
    Class<? extends Behavior> _behaviorKey = behavior.getBehaviorKey();
    return this.behaviors.put(_behaviorKey, behavior);
  }
  
  public Behavior removeBehavior(final String key) {
    return this.behaviors.remove(key);
  }
  
  protected void addArrowHead(final Property<? extends Node> property, final ChangeListener<? super Node> listener) {
    boolean _and = false;
    Node _value = property.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (!_notEquals) {
      _and = false;
    } else {
      Group _connectionLayer = this.getConnectionLayer();
      ObservableList<Node> _children = _connectionLayer.getChildren();
      Node _value_1 = property.getValue();
      boolean _contains = _children.contains(_value_1);
      boolean _not = (!_contains);
      _and = _not;
    }
    if (_and) {
      Group _connectionLayer_1 = this.getConnectionLayer();
      ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
      Node _value_2 = property.getValue();
      _children_1.add(_value_2);
    }
    property.addListener(listener);
  }
  
  public AuxiliaryLinesSupport getAuxiliaryLinesSupport() {
    return this.auxiliaryLinesSupport;
  }
  
  public Iterable<XShape> getAllShapes() {
    Iterable<? extends Node> _allChildren = CoreExtensions.getAllChildren(this);
    return Iterables.<XShape>filter(_allChildren, XShape.class);
  }
  
  public Procedure1<? super XDiagram> setContentsInitializer(final Procedure1<? super XDiagram> contentsInitializer) {
    return this.contentsInitializer = contentsInitializer;
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    Group _xifexpression = null;
    boolean _isRootDiagram = this.getIsRootDiagram();
    if (_isRootDiagram) {
      _xifexpression = this.nodeLayer;
    } else {
      XDiagram _parentDiagram = this.getParentDiagram();
      _xifexpression = _parentDiagram.nodeLayer;
    }
    return _xifexpression;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(nodesProperty, XNode.class);
    modelElement.addProperty(connectionsProperty, XConnection.class);
    modelElement.addProperty(parentDiagramProperty, XDiagram.class);
  }
  
  private SimpleListProperty<XNode> nodesProperty = new SimpleListProperty<XNode>(this, "nodes",_initNodes());
  
  private static final ObservableList<XNode> _initNodes() {
    ObservableList<XNode> _observableArrayList = FXCollections.<XNode>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XNode> getNodes() {
    return this.nodesProperty.get();
  }
  
  public ListProperty<XNode> nodesProperty() {
    return this.nodesProperty;
  }
  
  private SimpleListProperty<XConnection> connectionsProperty = new SimpleListProperty<XConnection>(this, "connections",_initConnections());
  
  private static final ObservableList<XConnection> _initConnections() {
    ObservableList<XConnection> _observableArrayList = FXCollections.<XConnection>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XConnection> getConnections() {
    return this.connectionsProperty.get();
  }
  
  public ListProperty<XConnection> connectionsProperty() {
    return this.connectionsProperty;
  }
  
  private SimpleObjectProperty<ObservableMap<Node, Pos>> fixedButtonsProperty = new SimpleObjectProperty<ObservableMap<Node, Pos>>(this, "fixedButtons",_initFixedButtons());
  
  private static final ObservableMap<Node, Pos> _initFixedButtons() {
    HashMap<Node, Pos> _newHashMap = CollectionLiterals.<Node, Pos>newHashMap();
    ObservableMap<Node, Pos> _observableMap = FXCollections.<Node, Pos>observableMap(_newHashMap);
    return _observableMap;
  }
  
  public ObservableMap<Node, Pos> getFixedButtons() {
    return this.fixedButtonsProperty.get();
  }
  
  public void setFixedButtons(final ObservableMap<Node, Pos> fixedButtons) {
    this.fixedButtonsProperty.set(fixedButtons);
  }
  
  public ObjectProperty<ObservableMap<Node, Pos>> fixedButtonsProperty() {
    return this.fixedButtonsProperty;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyBooleanWrapper isRootDiagramProperty = new ReadOnlyBooleanWrapper(this, "isRootDiagram");
  
  public boolean getIsRootDiagram() {
    return this.isRootDiagramProperty.get();
  }
  
  public ReadOnlyBooleanProperty isRootDiagramProperty() {
    return this.isRootDiagramProperty.getReadOnlyProperty();
  }
  
  private SimpleBooleanProperty isLayoutOnActivateProperty = new SimpleBooleanProperty(this, "isLayoutOnActivate");
  
  public boolean getIsLayoutOnActivate() {
    return this.isLayoutOnActivateProperty.get();
  }
  
  public void setIsLayoutOnActivate(final boolean isLayoutOnActivate) {
    this.isLayoutOnActivateProperty.set(isLayoutOnActivate);
  }
  
  public BooleanProperty isLayoutOnActivateProperty() {
    return this.isLayoutOnActivateProperty;
  }
  
  private SimpleObjectProperty<Paint> backgroundPaintProperty = new SimpleObjectProperty<Paint>(this, "backgroundPaint",_initBackgroundPaint());
  
  private static final Paint _initBackgroundPaint() {
    return Color.WHITE;
  }
  
  public Paint getBackgroundPaint() {
    return this.backgroundPaintProperty.get();
  }
  
  public void setBackgroundPaint(final Paint backgroundPaint) {
    this.backgroundPaintProperty.set(backgroundPaint);
  }
  
  public ObjectProperty<Paint> backgroundPaintProperty() {
    return this.backgroundPaintProperty;
  }
  
  private SimpleObjectProperty<Paint> foregroundPaintProperty = new SimpleObjectProperty<Paint>(this, "foregroundPaint",_initForegroundPaint());
  
  private static final Paint _initForegroundPaint() {
    return Color.BLACK;
  }
  
  public Paint getForegroundPaint() {
    return this.foregroundPaintProperty.get();
  }
  
  public void setForegroundPaint(final Paint foregroundPaint) {
    this.foregroundPaintProperty.set(foregroundPaint);
  }
  
  public ObjectProperty<Paint> foregroundPaintProperty() {
    return this.foregroundPaintProperty;
  }
  
  private SimpleObjectProperty<Paint> connectionPaintProperty = new SimpleObjectProperty<Paint>(this, "connectionPaint",_initConnectionPaint());
  
  private static final Paint _initConnectionPaint() {
    Color _gray = Color.gray(0.2);
    return _gray;
  }
  
  public Paint getConnectionPaint() {
    return this.connectionPaintProperty.get();
  }
  
  public void setConnectionPaint(final Paint connectionPaint) {
    this.connectionPaintProperty.set(connectionPaint);
  }
  
  public ObjectProperty<Paint> connectionPaintProperty() {
    return this.connectionPaintProperty;
  }
  
  private ReadOnlyObjectWrapper<XDiagram> parentDiagramProperty = new ReadOnlyObjectWrapper<XDiagram>(this, "parentDiagram");
  
  public XDiagram getParentDiagram() {
    return this.parentDiagramProperty.get();
  }
  
  public ReadOnlyObjectProperty<XDiagram> parentDiagramProperty() {
    return this.parentDiagramProperty.getReadOnlyProperty();
  }
}
