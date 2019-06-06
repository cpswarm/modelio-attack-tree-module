package org.modelio.module.attacktreedesigner.command.tools;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultMultiLinkTool;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("cb97a70e-ae73-4525-b1dd-eb1583ad95e0")
public class AndMultiLinkTool extends DefaultMultiLinkTool {
    @objid ("c64e00aa-e402-4343-9189-01f63424add3")
    @Override
    public boolean acceptFirstElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        return (element instanceof Class 
                && (((Class) element).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODEWITHMASKABLEDESCENDANTS))) ;
    }

    @objid ("a3f48339-9019-414d-930e-1f89bf204f13")
    @Override
    public boolean acceptAdditionalElement(IDiagramHandle arg0, List<IDiagramGraphic> arg1, IDiagramGraphic arg2) {
        // TODO Auto-generated method stub
        return true;
    }

    @objid ("70ff79f3-7b2a-4c7b-8000-94fc0c9f8063")
    @Override
    public boolean acceptLastElement(IDiagramHandle arg0, List<IDiagramGraphic> arg1, IDiagramGraphic arg2) {
        // TODO Auto-generated method stub
        return true;
    }

    @objid ("b4184827-33fa-4f24-ba1b-594286f23e35")
    @Override
    public void actionPerformed(IDiagramHandle arg0, IDiagramGraphic arg1, List<IDiagramGraphic> arg2, List<LinkRouterKind> arg3, List<ILinkPath> arg4, Rectangle arg5) {
        // TODO Auto-generated method stub
    }

}
