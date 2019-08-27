package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IMetamodelExtensions;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.vcore.smkernel.mapi.MMetamodel;

/**
 * @author ebrosse
 */
@objid ("585331fa-e4a5-46fd-9db8-4fdb5ff9db16")
public class AttackTreePropertyManager {
    /**
     * @param MObject
     * : the selected MObject
     * @param row : the row of the property
     * @param value : the new value of the property
     * @return the new value of the row
     */
    @objid ("e260ec4e-bd7f-42c4-bc71-11cfa2e36aad")
    public int changeProperty(ModelElement element, int row, String value) {
        IPropertyContent propertyPage = null;
        IMetamodelExtensions extensions = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions();
        
        int currentRow = row;
        MMetamodel metamodel = AttackTreeDesignerModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel();
        
        for (Stereotype stereotype : element.getExtension()) {
        
            // Attack property page
            if (stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, metamodel.getMClass(Class.class)))
                    || stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT, metamodel.getMClass(Class.class)))) {
                propertyPage = new AttackPropertyPage();
                propertyPage.changeProperty(element, currentRow, value);
                currentRow = currentRow - stereotype.getDefinedTagType().size();
        
                //Leaf property page
                if (element.getDependsOnDependency().size() == 0){ 
                    propertyPage = new LeafPropertyPage();
                    propertyPage.changeProperty(element, currentRow, value);
                    currentRow = currentRow - 1;
                }
            }
        
            // Condition property page
            if ((stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR, metamodel.getMClass(Class.class))))
                    ||(stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND, metamodel.getMClass(Class.class))))) {
                propertyPage = new OperatorPropertyPage();
                propertyPage.changeProperty(element, currentRow, value);
                currentRow = currentRow - 1;
            }
        
            // Reference tree attribute property page
            if(stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE, metamodel.getMClass(Attribute.class)))){
                propertyPage = new LeafPropertyPage();
                propertyPage.changeProperty(((Attribute)element).getOwner(), currentRow, value);
                currentRow = currentRow - 1;
            }
        
        }
        return currentRow;
    }

    /**
     * build the property table of the selected Elements
     * @param MObject
     * : the selected element
     * @param table : the property table
     */
    @objid ("559b50e5-a782-412a-9a95-fc8627bf6297")
    public void update(ModelElement element, IModulePropertyTable table) {
        IMetamodelExtensions extensions = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions();
        IPropertyContent propertyPage = null;
        MMetamodel metamodel = AttackTreeDesignerModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel();
        
        for (Stereotype stereotype : element.getExtension()) {
        
            // Attack property page
            if (stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, metamodel.getMClass(Class.class)))
                    || stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT, metamodel.getMClass(Class.class)))) {
        
                propertyPage = new AttackPropertyPage();
                propertyPage.update(element, table);
        
                //Leaf property page
                if (! stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT, metamodel.getMClass(Class.class))) 
                        && element.getDependsOnDependency().size() == 0){ 
                    propertyPage = new LeafPropertyPage();
                    propertyPage.update(element, table);
                }
            }
        
        
            // Condition property page
            if ((stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR, metamodel.getMClass(Class.class))))
                    ||(stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND, metamodel.getMClass(Class.class))))) {
                propertyPage = new OperatorPropertyPage();
                propertyPage.update(element, table);
            }
        
            // Reference tree attribute property page
            if(stereotype.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE, metamodel.getMClass(Attribute.class)))){
                propertyPage = new LeafPropertyPage();
                propertyPage.update(((Attribute)element).getOwner(), table);
        
            }
        
        }
    }

}
