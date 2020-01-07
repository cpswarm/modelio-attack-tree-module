package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeModelChangeHandler;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.ElementRepresentationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("bca5a48e-7295-4265-b0f2-e7a1a20607dc")
public class ElementReferencing {
    @objid ("f2d82ec6-ea30-49ef-a7be-8b5e8011bbaa")
    public static final String REF_DEFAULT_NAME = "ref";

    @objid ("abf2f19b-1e2e-4175-8da0-6cfa944df18e")
    private static final String PATH_SEPARATOR = "/";

    @objid ("bcc90451-b1e2-413b-834b-bb3425131013")
    private static List<Class> _availableTrees = null;

    @objid ("3056ec3a-3f3f-416f-8875-f0aa37d93ce4")
    public static List<String> getAvailableTreesFullPath() {
        List<String> availableTreeNames = new ArrayList<>();
        for(Class tree : _availableTrees) {           
            //            availableTreeNames.add(tree.getOwner().getName() + "::" +  tree.getName());        
            availableTreeNames.add(getElementFullPath(tree));        
        }
        return availableTreeNames;
    }

    /**
     * @param selectedElement
     * @return List of trees roots contained in the same package as the root of the selectedElement
     */
    @objid ("3e22b7bc-fb49-49c7-a5de-a139307b12d9")
    public static void updateListOfAvailableTrees(ModelTree selectedElement) {
        _availableTrees = new ArrayList<>();
        
        ModelTree rootElement = ElementNavigationManager.getRootElement(selectedElement);
        
        for(Class clazz: AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().findByClass(Class.class)) {
            if(clazz.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT) 
                    && (!(clazz.equals(rootElement)))) {
                _availableTrees.add(clazz);
            }
        }
    }

    @objid ("9333b99d-75ca-4cc1-95d3-e0009d44c934")
    public static void updateReference(Class element, String referencedTreeName) {
        IUmlModel model = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().getModel();
        
        Class referencedTree = null;       
        
        if (!(referencedTreeName.equals("")))
            referencedTree = getTreeByAbsolutePathName(referencedTreeName);
        
        Attribute referenceAttribute = getRefAttribute(element);
        
        
        
        if(referenceAttribute != null) {
            if (referencedTree != null) {            
        
                referenceAttribute.setType(referencedTree);       
        
                if(CounterMeasureManager.isCountered(referencedTree, true)) {
                    ElementRepresentationManager.setClassColor(element, ElementRepresentationManager.COUNTERED_ATTACK_COLOR);
        
        
                } else {
                    ElementRepresentationManager.setClassColor(element, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);
                }
            } else {             
                referenceAttribute.setType(model.getUmlTypes().getUNDEFINED());
                ElementRepresentationManager.setClassColor(element, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);
            }
        }
        /*
         * Propagate to ascendants of reference
         */
        for(Dependency parentDependency : element.getImpactedDependency()) {
            if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                Class elementParent = (Class) parentDependency.getImpacted();                
                AttackTreeModelChangeHandler.updateAndPropagateAttackTags(elementParent, false, false, true);                         
            }
        }
    }

    @objid ("0056c4aa-9142-4f32-a3f0-8f37c3d54817")
    private static Class getTreeByAbsolutePathName(String referencedTreeName) {
        for (Class tree : _availableTrees) {
            if (getElementFullPath(tree).equals(referencedTreeName))
                return tree;
        }
        return null;
    }

    @objid ("cc5fdae9-6de0-4103-8986-622fe9616dd0")
    public static Attribute getRefAttribute(Classifier leaf) {
        for (Attribute attribute : leaf.getOwnedAttribute()) {
            if (attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE))
                return attribute;
        }
        return null;
    }

    @objid ("d00a4c28-6562-452c-8902-09fe41d9a7d3")
    public static Class getReferencedTree(Class reference) {
        List<Attribute> attributes = reference.getOwnedAttribute();
        
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

    @objid ("13b2fa67-bd54-4605-a525-b2b0372e8af8")
    public static String getElementFullPath(MObject element) {
        String fullPath = element.getName();
        MObject owner = element.getCompositionOwner();
        if(owner != null) {
            return getElementFullPath(owner) + PATH_SEPARATOR + fullPath;
        } else {
            return fullPath;
        }
    }

}
