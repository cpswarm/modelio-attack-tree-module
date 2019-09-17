package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.ElementRepresentationManager;

@objid ("bca5a48e-7295-4265-b0f2-e7a1a20607dc")
public class ElementReferencing {
    @objid ("f2d82ec6-ea30-49ef-a7be-8b5e8011bbaa")
    private static final String REF_DEFAULT_NAME = "ref";

    @objid ("bcc90451-b1e2-413b-834b-bb3425131013")
    private static List<Class> _roots = null;

    @objid ("3056ec3a-3f3f-416f-8875-f0aa37d93ce4")
    public static List<String> getAvailableTreesNames() {
        List<String> result = new ArrayList<>();
        for(Class root : _roots) {           
            result.add(root.getOwner().getName() + "::" +  root.getName());        
        }
        return result;
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
    public static void addReference(Classifier selectedElement, String referencedTreeName) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        
        GeneralClass referencedTree = null;       
        
        if (!(referencedTreeName.equals("")))
            referencedTree = getTreeByName(referencedTreeName);
        
        if (referencedTree != null) {
        
            /*
             *  Referenced Tree is not null, update reference attribute
             */
            
            Attribute ref = getRefAttribute(selectedElement);
            
            if (ref == null) {
        
                // Create new Attribute
                Attribute referenceTreeAttribute = moduleContext.getModelingSession().getModel().createAttribute(
                        REF_DEFAULT_NAME, referencedTree, selectedElement, IAttackTreeDesignerPeerModule.MODULE_NAME, 
                        AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE); 
                
                // add stereotype of TREE_REFERENCE_DECORATION
                selectedElement.addStereotype(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
                
                // Update style to referencing tree attack style
                ElementRepresentationManager.changeStyleToReferencedTree(selectedElement, moduleContext, referenceTreeAttribute);
             
            }else {
                // Attribute already created, need only to change its type (i.e. its reference)
                ref.setType(referencedTree);
            }
        }else {
            
            /*
             *  Referenced Tree is null, delete TREE_REFERENCE_ATTRIBUTE
             */
            
            // Delete Attribute
            deleteAttribute(selectedElement);
            
            // Remove TREE_REFERENCE_DECORATION Stereotype from Class
            selectedElement.removeStereotypes(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
            
            // Upddate to Style of a simple (non referencing) attack 
            ElementRepresentationManager.changeStyleToAttack(selectedElement, moduleContext);
        }
    }

    @objid ("dda1fa11-c55a-479c-a0ed-b34255991f5a")
    public static void deleteAttribute(Classifier selectedElement) {
        List<Attribute> attributes = selectedElement.getOwnedAttribute();               
        Iterator<Attribute> iterator = attributes.iterator();        
        while ( iterator.hasNext() ) {
            Attribute attribute = iterator.next();
            if (attribute.isStereotyped( IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                iterator.remove();
                attribute.delete();
            }
        }
    }

    @objid ("0056c4aa-9142-4f32-a3f0-8f37c3d54817")
    private static GeneralClass getTreeByName(String referencedTreeName) {
        for (Class root : _roots) {
            if (getStandardName(root).equals(referencedTreeName))
                return root;
        }
        return null;
    }

    @objid ("b672dc6a-c41b-498b-9e37-21dc8a9b2852")
    private static String getStandardName(GeneralClass root) {
        return root.getOwner().getName() + "::" + root.getName();
    }

    @objid ("cc5fdae9-6de0-4103-8986-622fe9616dd0")
    private static Attribute getRefAttribute(Classifier leaf) {
        for (Attribute attr : leaf.getOwnedAttribute()) {
            if (attr.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE))
                return attr;
        }
        return null;
    }

    @objid ("78d9ba1d-875d-4e46-bf30-04f1fc357284")
    public static String getReferencedTreeName(Class selectedElement) {
        List<Attribute> attributes = selectedElement.getOwnedAttribute();
        
        for(Attribute attribute : attributes) {
            if (attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                GeneralClass type = attribute.getType();
                if ((type != null)
                        && (type.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT))){
                    return getStandardName(type);
                }
            }
        }
        return "";
    }

}
