package org.modelio.module.attacktreedesigner.property;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.propertiesPage.AbstractModulePropertyPage;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.AttackTreeResourcesManager;
import org.modelio.module.attacktreedesigner.utils.TagsManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("23bf4eaa-6db6-4138-8664-6b16f27ded1e")
public class AttackTreePropertyPage extends AbstractModulePropertyPage {
    @objid ("dcfd9796-8fbf-438d-b23b-905ef12af0a2")
    public static final String NAME_PROPERTY = "Name";

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
        if ((selectedElements != null) && (selectedElements.size() > 0)) {
            MObject selectedElement = selectedElements.get(0);
            if( (selectedElement != null)
                    && (selectedElement instanceof Class)
                    && ((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)){
        
                // add Name property
                table.addProperty (AttackTreeResourcesManager.getInstance().getPropertyName(NAME_PROPERTY), selectedElement.getName());
        
                // add tags properties
                List<TaggedValue> listTags = ((Class) selectedElement).getTag();
        
                for(TaggedValue tag:listTags) {
                    if(tag.getDefinition().getName().equals(AttackTreeTagTypes.SEVERITY)) {
        
                        table.addProperty (tag.getDefinition().getName(), 
                                TagsManager.getParameter(tag, tag.getDefinition().getName()),
                                TagsManager.SEVERITY_VALUES);
        
                    } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.PROBABILITY)){
                        table.addProperty (tag.getDefinition().getName(), 
                                TagsManager.getParameter(tag, tag.getDefinition().getName()),
                                TagsManager.PROBABILITY_VALUES);
                    }
                    else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.RISK_LEVEL)) {
                        table.addConsultProperty (tag.getDefinition().getName(), 
                                TagsManager.getParameter(tag, tag.getDefinition().getName()));
                    } 
        
                    // TODO Add remaining properties for the other tags
        
                }
        
            }
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
        if ((selectedElements != null) && (selectedElements.size() > 0)) {
            MObject selectedElement = selectedElements.get(0);
            if( (selectedElement != null)
                    && (selectedElement instanceof Class)
                    && ((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)){
        
        
                if(row==1) {
                    // row=1 -> Name property
                    selectedElement.setName(value);
        
                } else if (row == 2) {
                    // row=2 -> Severity property
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.SEVERITY, value);
        
                    // calculate Risk Level
                    TagsManager.updateRiskLevelTagValue(selectedElement);
        
        
                } else if (row == 3) {
                    // row=3 -> Probability property
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.PROBABILITY, value);
        
                    // calculate Risk Level
                    TagsManager.updateRiskLevelTagValue(selectedElement);
        
                } 
            }
        
        
            //ModelElement element = ((ModelElement) selectedElements.get (0));
        
        
            //AttackTreePropertyManager.changeProperty(element, row, value);
        
        }
    }

}
