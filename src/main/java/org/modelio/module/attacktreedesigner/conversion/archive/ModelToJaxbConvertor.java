package org.modelio.module.attacktreedesigner.conversion.archive;

import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeType;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeXMLObjectFactory;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackType;
import org.modelio.module.attacktreedesigner.conversion.schema.TreeDiagramType;

public class ModelToJaxbConvertor implements IConvertor {

    @Override
    public AttackTreeType convert(Object element) {
        if(element instanceof Class) {
            
            
            
            Class node = (Class) element;
            if(node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
                returnConvertAttack(node);
            } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR)) {
                returnConvertOR(node);
            } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND)) {
                returnConvertAND(node);
            } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
//                return convertRoot(node);
//            } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)) {
//                return ConvertTreeReference(node);
            }
            
        }  else if (element instanceof Package) {
            
        }
        return null;
        
    }

    private Object ConvertTreeReference(Class node) {
        // TODO Auto-generated method stub
        return null;
    }


    private void returnConvertAND(Class node) {
        // TODO Auto-generated method stub
        
    }

    private void returnConvertOR(Class node) {
        // TODO Auto-generated method stub
        
    }

    private void returnConvertAttack(Class node) {
        // TODO Auto-generated method stub
        
    }

    private Object convertRoot(Class node) {

        String treeName = "Tree Truc";

        AttackTreeXMLObjectFactory objectFactory = new AttackTreeXMLObjectFactory();

        AttackType attack = objectFactory.createAttackType();
        attack.setName(treeName);

        TreeDiagramType diagram = objectFactory.createTreeDiagramType();
        diagram.setName("Diag");

        AttackTreeType tree = objectFactory.createAttackTreeType();
        tree.setAttack(attack);
        tree.setTreeDiagram(diagram);

        
        return null;
    }


    
    
 
}
