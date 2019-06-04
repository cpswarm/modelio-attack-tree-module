package org.modelio.module.attacktreedesigner.command.diagram;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultMultiLinkTool;

@objid ("27b85b61-8018-4395-ae64-5fa9217ebc39")
public class AndTool extends DefaultMultiLinkTool {
    @objid ("7e7c80e6-fec9-4f28-aab2-b9cf64920718")
    @Override
    public boolean acceptFirstElement(final IDiagramHandle diagramHandle, final IDiagramGraphic targetNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("9bf960ae-2ec5-4e00-88f6-3e4c6b974887")
    @Override
    public boolean acceptAdditionalElement(final IDiagramHandle diagramHandle, final List<IDiagramGraphic> previousNodes, final IDiagramGraphic targetNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("3e7d07a7-4a38-467c-83d9-37ba1221704a")
    @Override
    public boolean acceptLastElement(final IDiagramHandle diagramHandle, final List<IDiagramGraphic> otherNodes, final IDiagramGraphic targetNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("2e8a89a5-f28b-4b35-ad94-fabb1bb30051")
    @Override
    public void actionPerformed(final IDiagramHandle diagramHandle, final IDiagramGraphic lastNode, final List<IDiagramGraphic> otherNodes, final List<LinkRouterKind> routerKinds, final List<ILinkPath> paths, final Rectangle rectangle) {
        // TODO implement the AndTool handler. 
        MessageDialog.openInformation(Display.getDefault().getActiveShell(), "AndTool", "Tool not implemented!");
    }

}
