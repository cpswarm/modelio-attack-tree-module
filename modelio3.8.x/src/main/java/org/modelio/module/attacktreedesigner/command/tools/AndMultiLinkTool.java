package org.modelio.module.attacktreedesigner.command.tools;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultMultiLinkTool;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementCreationManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementNavigationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("cb97a70e-ae73-4525-b1dd-eb1583ad95e0")
public class AndMultiLinkTool extends DefaultMultiLinkTool {
    @objid ("c64e00aa-e402-4343-9189-01f63424add3")
    @Override
    public boolean acceptFirstElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject firstElement = targetNode.getElement();
        if (firstElement instanceof Class 
                && ((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODE)
                && ! ((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE)
                && ! ((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)
                ){            
        
            // if it is an operator it is allowed to have many children
            if( ((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)){
                return true;
            }
            
            // It it is not an operator (only left is Attack), then it must have only one child
            for(Dependency dependency : (((ModelElement) firstElement).getDependsOnDependency())){
                if (dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @objid ("a3f48339-9019-414d-930e-1f89bf204f13")
    @Override
    public boolean acceptAdditionalElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> previousNodes, IDiagramGraphic targetNode) {
        MObject newElement = targetNode.getElement();
        if (newElement instanceof Class 
                && (((Class) newElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODE))
                && !(((Class) newElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT))
                && ! ElementNavigationManager.haveSameSecondLevelAncestor(previousNodes, targetNode)) {
        
        
            for(Dependency dependency : ((ModelElement) newElement).getImpactedDependency()){
                if (dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                    return false;
                }
            }
        
            return true;
        }
        return false;
    }

    @objid ("70ff79f3-7b2a-4c7b-8000-94fc0c9f8063")
    @Override
    public boolean acceptLastElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> otherNodes, IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        return (element instanceof AbstractDiagram);
    }

    @objid ("b4184827-33fa-4f24-ba1b-594286f23e35")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic lastNode, List<IDiagramGraphic> otherNodes, List<LinkRouterKind> routerKinds, List<ILinkPath> paths, Rectangle rectangle) {
        ElementCreationManager.createOperatorElement(diagramHandle, otherNodes, rectangle, AttackTreeStereotypes.AND);
    }

}
