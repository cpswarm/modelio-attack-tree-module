package org.modelio.module.attacktreedesigner.command.tools;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.tools.DefaultBoxTool;

@objid ("e0d3111f-5fa8-4890-ab5d-db90631a4a61")
public class CustomOrTool extends DefaultBoxTool {
    @objid ("965f5f3d-18b0-4032-bd1c-9a7a9a9a801b")
    @Override
    public boolean acceptElement(final IDiagramHandle diagramHandle, final IDiagramGraphic targetNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("0b100184-4051-4838-8512-1e7a81b15963")
    @Override
    public void actionPerformed(final IDiagramHandle diagramHandle, final IDiagramGraphic parent, final Rectangle rect) {
        // TODO implement the CustomOrTool handler. 
        MessageDialog.openInformation(Display.getDefault().getActiveShell(), "CustomOrTool", "Tool not implemented!");
    }

}
