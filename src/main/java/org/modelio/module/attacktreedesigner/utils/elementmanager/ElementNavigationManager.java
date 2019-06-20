package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;

@objid ("211fa652-db71-4a19-8253-a61ecfcff0e5")
public class ElementNavigationManager {
    @objid ("2243b459-a3a7-4221-b426-eb2b04e2574d")
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

    @objid ("1ba2a703-3ae4-45f4-835d-a2ad46d52f65")
    public static boolean haveSameSecondLevelAncestor(IDiagramGraphic firstNode, IDiagramGraphic secondNode) {
        return getSecondLevelAncestor((ModelTree) firstNode.getElement()).equals(getSecondLevelAncestor((ModelTree) secondNode.getElement()));
    }

    @objid ("fc2a167f-159a-443f-b57f-1ea9ac0b5073")
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
