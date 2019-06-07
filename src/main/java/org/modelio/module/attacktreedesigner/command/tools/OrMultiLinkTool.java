package org.modelio.module.attacktreedesigner.command.tools;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultMultiLinkTool;

@objid ("306d4fc0-625f-4bb7-9934-b8e01a97ce9f")
public class OrMultiLinkTool extends DefaultMultiLinkTool {
    @objid ("4a50c985-f95d-4c28-bba3-8a61b7abf167")
    @Override
    public boolean acceptFirstElement(IDiagramHandle arg0, IDiagramGraphic arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("ebfafd9f-995f-4219-b76b-9f27ea6d8f86")
    @Override
    public boolean acceptAdditionalElement(IDiagramHandle arg0, List<IDiagramGraphic> arg1, IDiagramGraphic arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("38d296c4-1868-4e48-a814-3d13f3763e3a")
    @Override
    public boolean acceptLastElement(IDiagramHandle arg0, List<IDiagramGraphic> arg1, IDiagramGraphic arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("7e23fc39-13ca-4e43-9370-396c5e87efe6")
    @Override
    public void actionPerformed(IDiagramHandle arg0, IDiagramGraphic arg1, List<IDiagramGraphic> arg2, List<LinkRouterKind> arg3, List<ILinkPath> arg4, Rectangle arg5) {
        // TODO Auto-generated method stub
    }

}
