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

@objid ("306d4fc0-625f-4bb7-9934-b8e01a97ce9f")
public class OrMultiLinkTool extends DefaultMultiLinkTool {
    @objid ("4a50c985-f95d-4c28-bba3-8a61b7abf167")
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

    @objid ("ebfafd9f-995f-4219-b76b-9f27ea6d8f86")
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

    @objid ("38d296c4-1868-4e48-a814-3d13f3763e3a")
    @Override
    public boolean acceptLastElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> otherNodes, IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        return (element instanceof AbstractDiagram);
    }

    @objid ("7e23fc39-13ca-4e43-9370-396c5e87efe6")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic lastNode, List<IDiagramGraphic> otherNodes, List<LinkRouterKind> routerKinds, List<ILinkPath> paths, Rectangle rectangle) {
        ElementCreationManager.createOperatorElement(diagramHandle, otherNodes, rectangle, AttackTreeStereotypes.OR);
    }

}
