package org.modelio.module.attacktreedesigner.utils;

import java.util.List;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;

public class ElementNavigationManager {

    public static ModelTree getSecondLevelAncestor(ModelTree node) {
        if(node instanceof Class) {
            if(((Class)node).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
                return node;
            }

            ModelTree nodeOwner = node.getOwner();
            if(nodeOwner instanceof Class && ((Class)nodeOwner).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
                return node;
            } else {
                return getSecondLevelAncestor(nodeOwner);
            }
        } else {
            return null;
        }
        
    }
    
    public static boolean haveSameSecondLevelAncestor(IDiagramGraphic firstNode, IDiagramGraphic secondNode) {
        return getSecondLevelAncestor((ModelTree) firstNode.getElement()).equals(getSecondLevelAncestor((ModelTree) secondNode.getElement()));
    }
    
    
    public static boolean haveSameSecondLevelAncestor(List<IDiagramGraphic> previousNodes, IDiagramGraphic newNode) {
        ModelTree newElementModelTree = (ModelTree) newNode.getElement();
        for(IDiagramGraphic currentDiagramGraphic: previousNodes) {
            if(getSecondLevelAncestor((ModelTree) currentDiagramGraphic.getElement()).equals(getSecondLevelAncestor(newElementModelTree))){
                return true;
            }
        }
        return false;
    }
}
