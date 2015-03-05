package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.eclipse.shapes.BaseConnection

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to an {@link XConnection}.
 * 
 * @see AbstractMapping
 */
class ConnectionMapping<T> extends AbstractMapping<T> {

	NodeMappingCall<?, T> source
	NodeMappingCall<?, T> target

	new(XDiagramConfig config, String id) {
		super(config, id)
	}

	/**
	 * Called to instantiate the {@link XConnection} of this mapping. Override 
	 * if you want to change the way the {@link XConnection} should look like,
	 * e.g. add a label or arrow heads.
	 */
	def XConnection createConnection(IMappedElementDescriptor<T> descriptor) {
		new BaseConnection(descriptor)
	}

	def getSource() { initialize ; source }

	def getTarget() { initialize ; target }

	def <U> source(NodeMapping<U> nodeMapping, (T)=>U selector) {
		source = new NodeMappingCall(selector, nodeMapping)
	}

	def <U> target(NodeMapping<U> nodeMapping, (T)=>U selector) {
		target = new NodeMappingCall(selector, nodeMapping)
	}
}