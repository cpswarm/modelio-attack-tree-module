package org.modelio.module.attacktreedesigner.property;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.propertiesPage.AbstractModulePropertyPage;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("23bf4eaa-6db6-4138-8664-6b16f27ded1e")
public class AttackTreePropertyPage extends AbstractModulePropertyPage {
    @objid ("dcfd9796-8fbf-438d-b23b-905ef12af0a2")
    public static final String NAME_PROPERTY = "Name";

    @objid ("50df2a0f-9be2-4a9f-8b25-6ceabfa60293")
    private static final String REFERENCING_A_TREE_LABEL = "Referencing a tree";

    @objid ("6e0f839a-3fec-4180-b2bd-de72fb2384d0")
    private static final String REFERENCED_TREE_LABEL = "Referenced tree";

    @objid ("801a3c4e-e87e-4923-82de-facf9fce16e6")
    public AttackTreePropertyPage(IModule module, String name, String label, String bitmap) {
        super (module, name, label, bitmap);
    }

    /**
     * This method is called when the current selection changes and that the property box contents requires an update.
     * The ?selectedElements? parameter contains the list of the newly selected elements. The ?table? parameter is the
     * table that must be filled with the updated contents of the property box before returning.
     */
    @objid ("d0ab204e-02c5-4689-aa0d-b68be90eaa15")
    @Override
    public void update(List<MObject> selectedElements, IModulePropertyTable table) {
        if ((selectedElements != null) && (selectedElements.size() > 0)
                && (selectedElements.get(0) != null)
                && (selectedElements.get(0) instanceof ModelElement)){
        
            ModelElement element = ((ModelElement) selectedElements.get(0));
            
            AttackTreePropertyManager attackTreePage = new AttackTreePropertyManager();
            attackTreePage.update(element, table);
        
        }
    }

    /**
     * This method is called when a value has been edited in the property box in the row ?row?. The ?selectedElements?
     * parameter contains the list of the currently selected elements. The ?row? parameter is the row number of the
     * modified value. The ?value? parameter is the new value the user has set to the given row.
     */
    @objid ("5cf28d81-b746-4c5c-acd9-c075bb6c4468")
    @Override
    public void changeProperty(List<MObject> selectedElements, int row, String value) {
        if ((selectedElements != null) && (selectedElements.size() > 0) && (selectedElements.get(0) instanceof ModelElement)){
            
            ModelElement element = ((ModelElement) selectedElements.get (0));
        
            AttackTreePropertyManager attackTreePage = new AttackTreePropertyManager();
            attackTreePage.changeProperty(element, row, value);
            
        }
    }

}
