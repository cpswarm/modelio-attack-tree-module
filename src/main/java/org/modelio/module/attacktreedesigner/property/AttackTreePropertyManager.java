package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("f97fbaac-1f51-49d9-88e4-4d123d39b79a")
public class AttackTreePropertyManager {
    /**
     * @param MObject
     * : the selected MObject
     * @param row : the row of the property
     * @param value : the new value of the property
     * @return the new value of the row
     */
    @objid ("686020f8-7fb4-414c-8694-d7f3fe888320")
    public int changeProperty(ModelElement element, int row, String value) {
        return row;
    }

    /**
     * build the property table of the selected Elements
     * @param MObject
     * : the selected element
     * @param table : the property table
     */
    @objid ("60d3e597-4f70-4498-840f-69a3140a067e")
    public void update(ModelElement element, IModulePropertyTable table) {
    }

}
