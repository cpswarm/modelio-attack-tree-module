package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class ElementReferencing {

    private static final String REF_DEFAULT_NAME = "ref";

    private static List<Class> _roots = null;
    
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
    

    private static GeneralClass getTreeByName(String referencedTreeName) {
        for (Class root : _roots) {
            if (getStandardName(root).equals(referencedTreeName))
                return root;
        }
        return null;
    }

    
    private static String getStandardName(GeneralClass root) {
        return root.getOwner().getName() + "::" + root.getName();
    }
    
    

    private static Attribute getRefAttribute(Classifier leaf) {
        for (Attribute attr : leaf.getOwnedAttribute()) {
            if (attr.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE))
                return attr;
        }
        return null;
    }
    
    
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
