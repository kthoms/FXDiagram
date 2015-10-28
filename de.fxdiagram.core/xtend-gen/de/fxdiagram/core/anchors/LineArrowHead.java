package de.fxdiagram.core.anchors;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class LineArrowHead extends ArrowHead {
  public LineArrowHead(final XConnection connection, final double width, final double height, final Paint stroke, final boolean isSource) {
    super(connection, width, height, stroke, isSource);
  }
  
  public LineArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 7, 10, null, isSource);
  }
  
  @Override
  public Node createNode() {
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      Polyline _polyline = new Polyline();
      final Procedure1<Polyline> _function_1 = (Polyline it_1) -> {
        ObservableList<Double> _points = it_1.getPoints();
        double _height = this.getHeight();
        double _multiply = ((-0.5) * _height);
        double _width = this.getWidth();
        double _height_1 = this.getHeight();
        double _multiply_1 = (0.5 * _height_1);
        _points.setAll(Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(_multiply), Double.valueOf(_width), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_multiply_1))));
        ObjectProperty<Paint> _strokeProperty = it_1.strokeProperty();
        ObjectProperty<Paint> _strokeProperty_1 = this.strokeProperty();
        _strokeProperty.bind(_strokeProperty_1);
        DoubleProperty _strokeWidthProperty = it_1.strokeWidthProperty();
        XConnection _connection = this.getConnection();
        DoubleProperty _strokeWidthProperty_1 = _connection.strokeWidthProperty();
        _strokeWidthProperty.bind(_strokeWidthProperty_1);
        DoubleProperty _opacityProperty = it_1.opacityProperty();
        XConnection _connection_1 = this.getConnection();
        DoubleProperty _opacityProperty_1 = _connection_1.opacityProperty();
        _opacityProperty.bind(_opacityProperty_1);
        it_1.setStrokeType(StrokeType.CENTERED);
      };
      Polyline _doubleArrow = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      Polyline _polyline_1 = new Polyline();
      final Procedure1<Polyline> _function_2 = (Polyline it_1) -> {
        ObservableList<Double> _points = it_1.getPoints();
        double _width = this.getWidth();
        XConnection _connection = this.getConnection();
        double _strokeWidth = _connection.getStrokeWidth();
        double _minus = (_width - _strokeWidth);
        _points.setAll(Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_minus), Double.valueOf(0.0))));
        ObjectProperty<Paint> _strokeProperty = it_1.strokeProperty();
        ObjectProperty<Paint> _strokeProperty_1 = this.strokeProperty();
        _strokeProperty.bind(_strokeProperty_1);
        DoubleProperty _strokeWidthProperty = it_1.strokeWidthProperty();
        XConnection _connection_1 = this.getConnection();
        DoubleProperty _strokeWidthProperty_1 = _connection_1.strokeWidthProperty();
        _strokeWidthProperty.bind(_strokeWidthProperty_1);
        DoubleProperty _opacityProperty = it_1.opacityProperty();
        XConnection _connection_2 = this.getConnection();
        DoubleProperty _opacityProperty_1 = _connection_2.opacityProperty();
        _opacityProperty.bind(_opacityProperty_1);
        it_1.setStrokeType(StrokeType.CENTERED);
      };
      Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline_1, _function_2);
      _children_1.add(_doubleArrow_1);
    };
    return ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
  }
  
  @Override
  public double getLineCut() {
    double _width = this.getWidth();
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    return (_width + _strokeWidth);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LineArrowHead() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
