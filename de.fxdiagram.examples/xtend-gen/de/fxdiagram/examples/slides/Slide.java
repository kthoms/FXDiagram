package de.fxdiagram.examples.slides;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class Slide extends XNode {
  private ImageView imageView;
  
  private Procedure0 onActivate;
  
  public Slide(final String name) {
    super(name);
  }
  
  public Slide(final String name, final Image backgroundImage) {
    this(name);
    this.setBackgroundImage(backgroundImage);
  }
  
  @Override
  protected StackPane createNode() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      ImageView _imageView = new ImageView();
      final Procedure1<ImageView> _function_1 = (ImageView it_1) -> {
        ColorAdjust _colorAdjust = new ColorAdjust();
        final Procedure1<ColorAdjust> _function_2 = (ColorAdjust it_2) -> {
          it_2.setBrightness((-0.5));
          it_2.setSaturation(0);
          it_2.setContrast((-0.1));
        };
        ColorAdjust _doubleArrow = ObjectExtensions.<ColorAdjust>operator_doubleArrow(_colorAdjust, _function_2);
        it_1.setEffect(_doubleArrow);
      };
      ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_1);
      _children.add((this.imageView = _doubleArrow));
    };
    return ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    Image _backgroundImage = this.getBackgroundImage();
    this.imageView.setImage(_backgroundImage);
    ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
    XRoot _root = CoreExtensions.getRoot(this);
    _zoomToFitAction.perform(_root);
    if (this.onActivate!=null) {
      this.onActivate.apply();
    }
  }
  
  public Procedure0 setOnActivate(final Procedure0 onActivate) {
    return this.onActivate = onActivate;
  }
  
  public StackPane getStackPane() {
    Node _node = this.getNode();
    return ((StackPane) _node);
  }
  
  @Override
  public void selectionFeedback(final boolean isSelected) {
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public Slide() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  private SimpleObjectProperty<Image> backgroundImageProperty = new SimpleObjectProperty<Image>(this, "backgroundImage");
  
  public Image getBackgroundImage() {
    return this.backgroundImageProperty.get();
  }
  
  public void setBackgroundImage(final Image backgroundImage) {
    this.backgroundImageProperty.set(backgroundImage);
  }
  
  public ObjectProperty<Image> backgroundImageProperty() {
    return this.backgroundImageProperty;
  }
}
