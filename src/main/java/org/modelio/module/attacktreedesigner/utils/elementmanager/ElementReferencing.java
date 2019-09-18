package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;

@objid ("bca5a48e-7295-4265-b0f2-e7a1a20607dc")
public class ElementReferencing {
    @objid ("f2d82ec6-ea30-49ef-a7be-8b5e8011bbaa")
    private static final String REF_DEFAULT_NAME = "ref";

    @objid ("bcc90451-b1e2-413b-834b-bb3425131013")
    private static List<Class> _roots = null;

    @objid ("3056ec3a-3f3f-416f-8875-f0aa37d93ce4")
    public static List<String> getAvailableTreesNames() {
        List<String> availableTreeNames = new ArrayList<>();
        for(Class root : _roots) {           
            availableTreeNames.add(root.getOwner().getName() + "::" +  root.getName());        
        }
        return availableTreeNames;
    }

    /**
     * @param selectedElement
     * @return List of trees roots contained in the same package as the root of the selectedElement
     */
    @objid ("3e22b7bc-fb49-49c7-a5de-a139307b12d9")
    public static void getAvailableTrees(ModelTree selectedElement) {
        _roots = new ArrayList<>();
        
        ModelTree rootElement = ElementNavigationManager.getRootElement(selectedElement);
        
        for(Class clazz: AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().findByClass(Class.class)) {
            if(clazz.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT) 
                    && (!(clazz.equals(rootElement)))) {
                _roots.add(clazz);
            }
        }
    }

    @objid ("9333b99d-75ca-4cc1-95d3-e0009d44c934")
    public static void updateReference(Classifier selectedElement, String referencedTreeName) {
        IUmlModel model = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().getModel();
        
        GeneralClass referencedTree = null;       
        
        if (!(referencedTreeName.equals("")))
            referencedTree = getTreeByName(referencedTreeName);
        
        Attribute referenceAttribute = getRefAttribute(selectedElement);
        
        if (referencedTree != null)             
            referenceAttribute.setType(referencedTree);            
        else             
            referenceAttribute.setType(model.getUmlTypes().getUNDEFINED());
    }

    @objid ("0056c4aa-9142-4f32-a3f0-8f37c3d54817")
    private static GeneralClass getTreeByName(String referencedTreeName) {
        for (Class tree : _roots) {
            if (getStandardName(tree).equals(referencedTreeName))
                return tree;
        }
        return null;
    }

    @objid ("b672dc6a-c41b-498b-9e37-21dc8a9b2852")
    public static String getStandardName(Class root) {
        return root.getOwner().getName() + "::" + root.getName();
    }

    @objid ("cc5fdae9-6de0-4103-8986-622fe9616dd0")
    private static Attribute getRefAttribute(Classifier leaf) {
        for (Attribute attribute : leaf.getOwnedAttribute()) {
            if (attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE))
                return attribute;
        }
        return null;
    }

    @objid ("d00a4c28-6562-452c-8902-09fe41d9a7d3")
    public static Class getReferencedTree(Class selectedElement) {
        List<Attribute> attributes = selectedElement.getOwnedAttribute();
        
        for(Attribute attribute : attributes) {
            if (attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                GeneralClass type = attribute.getType();
                if ((type != null)
                        && (type.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT))){
                    return (Class) type;
                }
            }
        }
        return null;
    }

}
