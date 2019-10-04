package org.modelio.module.attacktreedesigner.conversion;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
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

@objid ("7576d0d6-54bd-480d-b8d8-5fec7597d807")
public class ModelToJaxbConvertor {
    @objid ("0f3c064b-7d12-4bfe-b8c0-93e3dda47de9")
    private static AttackTreeXMLObjectFactory objectFactory = new AttackTreeXMLObjectFactory();

    @objid ("c596db91-d9e8-46e0-b03a-a700bec3cb3a")
    private Class modelTree;

    @objid ("54d80aa2-3e79-4372-96a7-6089709fca07")
    public Class getModelTree() {
        return this.modelTree;
    }

    @objid ("f70bb9b8-f7eb-4443-bd41-d48c13d403fd")
    public void setModelTree(Class modelTree) {
        this.modelTree = modelTree;
    }

    @objid ("ec312248-9be1-4398-9828-2533395830dd")
    public ModelToJaxbConvertor(Class modelTree) {
        this.modelTree = modelTree;
    }

    @objid ("568ee29e-1504-477c-aee1-0b268b626f57")
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

    @objid ("dbb61575-17b7-4d61-bfe1-67402492b5ab")
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

    @objid ("a690e78d-4f6b-4394-97eb-bf973a90aeb0")
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
                                treeReference.setRef(ElementReferencing.getTreeFullPath(referencedTree));
                                operator.getAttackOrTreeReference().add(treeReference);
                            }
                        }
                    }
                }        
            }                
        }
    }

}
