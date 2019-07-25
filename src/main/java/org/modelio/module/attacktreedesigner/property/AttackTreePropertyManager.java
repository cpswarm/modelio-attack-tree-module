package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IMetamodelExtensions;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
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
        IPropertyContent propertypage = null;
        IMetamodelExtensions extensions = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions();
        
        int currentRow = row;
        MMetamodel metamodel = AttackTreeDesignerModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel();
        
        for (Stereotype ster : element.getExtension()) {
        
            // Attack property page
            if (ster.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK,
                    metamodel.getMClass(Class.class)))) {
                propertypage = new AttackPropertyPage();
                propertypage.changeProperty(element, currentRow, value);
                currentRow = currentRow - ster.getDefinedTagType().size();
        
                //Leaf property page
                if (element.getDependsOnDependency().size() == 0){ 
                    propertypage = new LeafPropertyPage();
                    propertypage.changeProperty(element, currentRow, value);
                    currentRow = currentRow - 1;
                }
            }
        
            // Condition property page
            if ((ster.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR, metamodel.getMClass(Class.class))))
                    ||(ster.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND, metamodel.getMClass(Class.class))))) {
                propertypage = new OperatorPropertyPage();
                propertypage.changeProperty(element, currentRow, value);
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
        IPropertyContent propertypage = null;
        MMetamodel metamodel = AttackTreeDesignerModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel();
        
        for (Stereotype ster : element.getExtension()) {
        
            // Attack property page
            if (ster.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK,
                    metamodel.getMClass(Class.class)))) {
        
                propertypage = new AttackPropertyPage();
                propertypage.update(element, table);
        
                //Leaf property page
                if (element.getDependsOnDependency().size() == 0){ 
                    propertypage = new LeafPropertyPage();
                    propertypage.update(element, table);
                }
            }
        
        
            // Condition property page
                        if ((ster.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR, metamodel.getMClass(Class.class))))
                                ||(ster.equals(extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND, metamodel.getMClass(Class.class))))) {
                           propertypage = new OperatorPropertyPage();
                propertypage.update(element, table);
            }
        }
    }

}
