package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.eclipse.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.AbstractNodeMappingCall;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.MultiConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.MultiNodeMappingCall;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.NodeMappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to an {@link XDiagram}.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public abstract class DiagramMapping<T extends Object> extends AbstractMapping<T> {
  private List<AbstractNodeMappingCall<?, T>> nodes = CollectionLiterals.<AbstractNodeMappingCall<?, T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<?, T>> connections = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  public DiagramMapping(final XDiagramConfig config, final String id) {
    super(config, id);
  }
  
  public List<AbstractNodeMappingCall<?, T>> getNodes() {
    List<AbstractNodeMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.nodes;
    }
    return _xblockexpression;
  }
  
  public List<AbstractConnectionMappingCall<?, T>> getConnections() {
    List<AbstractConnectionMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.connections;
    }
    return _xblockexpression;
  }
  
  public XDiagram createDiagram(final IMappedElementDescriptor<T> descriptor) {
    return new XDiagram();
  }
  
  public <U extends Object> boolean nodeFor(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends U> selector) {
    NodeMappingCall<U, T> _nodeMappingCall = new NodeMappingCall<U, T>(selector, nodeMapping);
    return this.nodes.add(_nodeMappingCall);
  }
  
  public <U extends Object> boolean connectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    ConnectionMappingCall<U, T> _connectionMappingCall = new ConnectionMappingCall<U, T>(selector, connectionMapping);
    return this.connections.add(_connectionMappingCall);
  }
  
  public <U extends Object> boolean nodeForEach(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiNodeMappingCall<U, T> _multiNodeMappingCall = new MultiNodeMappingCall<U, T>(selector, nodeMapping);
    return this.nodes.add(_multiNodeMappingCall);
  }
  
  public <U extends Object> boolean connectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiConnectionMappingCall<U, T> _multiConnectionMappingCall = new MultiConnectionMappingCall<U, T>(selector, connectionMapping);
    return this.connections.add(_multiConnectionMappingCall);
  }
}