package de.fxdiagram.pde

import de.fxdiagram.eclipse.commands.ISelectionExtractor
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IAdaptable
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IWorkbenchPart

import static extension org.eclipse.pde.core.plugin.PluginRegistry.*

class BundleSelectionExtractor implements ISelectionExtractor {
	
	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		val selection = activePart.site.selectionProvider.selection
		if(selection instanceof IStructuredSelection) {
			selection.iterator
				.filter(IAdaptable)
				.map[getAdapter(IProject) as IProject]
				.map[findModel.bundleDescription]
				.filterNull
				.toSet
				.forEach[acceptor.accept(it)]
		}
	}
	
}