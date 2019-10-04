package org.modelio.module.attacktreedesigner.conversion;

import java.util.List;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeNoteTypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeType;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeXMLObjectFactory;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackType;
import org.modelio.module.attacktreedesigner.conversion.schema.CounterMeasureType;
import org.modelio.module.attacktreedesigner.conversion.schema.OperationType;
import org.modelio.module.attacktreedesigner.conversion.schema.OperatorType;
import org.modelio.module.attacktreedesigner.conversion.schema.TagType;
import org.modelio.module.attacktreedesigner.conversion.schema.TreeDiagramType;
import org.modelio.module.attacktreedesigner.conversion.schema.TreeReferenceType;
import org.modelio.module.attacktreedesigner.utils.TagsManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementReferencing;
import org.modelio.vcore.smkernel.mapi.MObject;

public class ModelToJaxbConvertor {

    private static AttackTreeXMLObjectFactory objectFactory = new AttackTreeXMLObjectFactory();
    
    private Class modelTree;

    public Class getModelTree() {
        return this.modelTree;
    }

    public void setModelTree(Class modelTree) {
        this.modelTree = modelTree;
    }

    public ModelToJaxbConvertor(Class modelTree) {
        this.modelTree = modelTree;
    }

    public AttackTreeType convertModelToJaxb() {
        

        /*
         * Create Tree
         */ 
        AttackTreeType tree = objectFactory.createAttackTreeType();


        /*
         * Set Diagram
         */
        for(MObject child : this.modelTree.getCompositionChildren()) {
            if(child instanceof AbstractDiagram) {
                TreeDiagramType diagram = objectFactory.createTreeDiagramType();
                diagram.setName(child.getName());
                tree.setTreeDiagram(diagram);
                break;
            }
        }


        /*
         * Set Root Node
         */
        AttackType root = convertNode(this.modelTree);
        tree.setAttack(root);

        // Add children nodes 
        addChildrenNodes(this.modelTree, root);



        return tree;


    }

    private static AttackType convertNode(Class modelNode) {
        AttackType attack = objectFactory.createAttackType();
        attack.setName(modelNode.getName());
        
        TagType severityTag = objectFactory.createTagType();
        severityTag.setName(AttackTreeTagTypes.SEVERITY);
        severityTag.setValue(TagsManager.getElementTagParameter(modelNode, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY));
        attack.getTag().add(severityTag);
        
        TagType probabilityTag = objectFactory.createTagType();
        probabilityTag.setName(AttackTreeTagTypes.PROBABILITY);
        probabilityTag.setValue(TagsManager.getElementTagParameter(modelNode, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY));
        attack.getTag().add(probabilityTag);

        TagType securityRelatedTag = objectFactory.createTagType();
        securityRelatedTag.setName(AttackTreeTagTypes.SECURITY_RELATED);
        securityRelatedTag.setValue(TagsManager.getElementTagParameter(modelNode, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SECURITY_RELATED));
        attack.getTag().add(securityRelatedTag);
        
        TagType safetyRelatedTag = objectFactory.createTagType();
        safetyRelatedTag.setName(AttackTreeTagTypes.SAFETY_RELATED);
        safetyRelatedTag.setValue(TagsManager.getElementTagParameter(modelNode, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SAFETY_RELATED));
        attack.getTag().add(safetyRelatedTag);
        
        TagType outOfScopeTag = objectFactory.createTagType();
        outOfScopeTag.setName(AttackTreeTagTypes.OUT_OF_SCOPE);
        outOfScopeTag.setValue(TagsManager.getElementTagParameter(modelNode, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.OUT_OF_SCOPE));
        attack.getTag().add(outOfScopeTag);
        
        List<Note> nodeNotes = modelNode.getDescriptor();
        for(Note note:nodeNotes) {
            if(note.getModel().getName().equals(AttackTreeNoteTypes.COUNTER_MEASURE)) {
                CounterMeasureType counterMeasure = objectFactory.createCounterMeasureType();
                counterMeasure.setContent(note.getContent());
                attack.getCounterMeasure().add(counterMeasure);
            }
        }
        
        return attack;
    }

    private void addChildrenNodes(Class parentNode, AttackType root) {
        

        for(Dependency dependency : parentNode.getDependsOnDependency()) {
            if (dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                
                OperatorType operator = objectFactory.createOperatorType();
                
                Class operatorNode = (Class) dependency.getDependsOn();
                if(operatorNode.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR)) {
                    operator.setType(OperationType.OR);
                } else if (operatorNode.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND)) {
                    operator.setType(OperationType.AND);
                }                
                root.setOperator(operator);
                

                for(Dependency dependency2:operatorNode.getDependsOnDependency()) {
                    if (dependency2.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                        
                        Class node = (Class) dependency2.getDependsOn();
                        if(node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
                            AttackType attack = convertNode(node);
                            operator.getAttackOrTreeReference().add(attack);
                            addChildrenNodes(node, attack);
                        } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)) {
                            Class referencedTree = ElementReferencing.getReferencedTree(node);
                            if(referencedTree != null) {
                                TreeReferenceType treeReference = objectFactory.createTreeReferenceType();
                                treeReference.setRef(referencedTree.getName());
                                operator.getAttackOrTreeReference().add(treeReference);
                            }
                        }
                    }
                }
                //operator.getAttackOrTreeReference().add(e)

            }                
        }

    }
}
