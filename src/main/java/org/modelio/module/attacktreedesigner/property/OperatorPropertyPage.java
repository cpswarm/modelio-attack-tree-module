package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerPeerModule;

@objid ("16f35395-b210-4759-8df8-daff2c306980")
public class OperatorPropertyPage implements IPropertyContent {
    /**
     * This method handles the changes of the given property, identified by its row index, of a selected element
     * to a new value.
     * @param MObject : the selected element
     * 
     * @param row : the row of the changed property
     * @param value : the new value of the property
     */
    @objid ("3a5f1795-2279-4c0f-ae22-192981b72ede")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if( row == 1 ) {
        
            IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession ();
        
            element.removeStereotypes(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
            element.removeStereotypes(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR);
        
            if (value.equals(AttackTreeStereotypes.AND)) {
                element.addStereotype(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
                session.getModel().getDefaultNameService().setDefaultName(element, AttackTreeStereotypes.AND);
            }else if (value.equals(AttackTreeStereotypes.OR)) {
                element.addStereotype(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR);
                session.getModel().getDefaultNameService().setDefaultName(element, AttackTreeStereotypes.OR);
            }
        }
    }

    /**
     * This method handles the construction of the property table of a selected element
     * @param MObject : the selected element
     * 
     * @param table : the property table to fulfill
     */
    @objid ("c881fa9a-0f63-432a-a89b-b3b534dd5dc8")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        String[] values = {AttackTreeStereotypes.AND, AttackTreeStereotypes.OR  };                                             
        
        String value = "";
        if (element.isStereotyped(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND)) {
            value = AttackTreeStereotypes.AND;  
        }else if (element.isStereotyped(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR)) {
            value = AttackTreeStereotypes.OR;
        }
        
        table.addProperty (Messages.getString("Ui.Property.TypeCondition.Name"),
                value,
                values);
    }

}
