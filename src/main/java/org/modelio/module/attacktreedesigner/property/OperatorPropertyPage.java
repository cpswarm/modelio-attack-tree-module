package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerPeerModule;

@objid ("16f35395-b210-4759-8df8-daff2c306980")
public class OperatorPropertyPage implements IPropertyContent {
    @objid ("3a5f1795-2279-4c0f-ae22-192981b72ede")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if( row == 1 ) {
        
            element.removeStereotypes(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
            element.removeStereotypes(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR);
        
            if (value.equals("AND")) {
                element.addStereotype(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
            }else if (value.equals("OR")) {
                element.addStereotype(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR);
            }
        }
    }

    @objid ("c881fa9a-0f63-432a-a89b-b3b534dd5dc8")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        String[] values = {"AND", "OR"  };                                             
        
        String value = "";
        if (element.isStereotyped(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND)) {
            value = "AND";  
        }else if (element.isStereotyped(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR)) {
            value = "OR";
        }
        
        table.addProperty (Messages.getString("Ui.Property.TypeCondition.Name"),
                value,
                values);
    }

}
