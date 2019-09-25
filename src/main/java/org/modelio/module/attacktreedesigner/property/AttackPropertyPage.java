package org.modelio.module.attacktreedesigner.property;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.TagsManager;

@objid ("224ee252-26f1-4b33-9ecc-84279671ca6c")
public class AttackPropertyPage implements IPropertyContent {
    @objid ("2594a992-2f3e-4018-962a-cb950f5accaa")
    public static final int PROPERTIES_SIZE = 7;

    @objid ("0f24f03d-602a-4141-b7e2-13cf4b88e0c7")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if(row==1) {
            // row=1 -> Name property
            element.setName(value);
        
        } else if (row == 2) {
            // row=2 -> Severity property
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY, value);
        
        } else if (row == 3) {
            // row=3 -> Probability property
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY, value);
        
        } else if (row == 5) {
            // row=5 -> Security related
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SECURITY_RELATED, value);
        
        
        } else if (row == 6) {
            // row=6 -> Safety related
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SAFETY_RELATED, value);
        
        
        } else if (row == 7) {
            // row=7 -> Out of scope
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.OUT_OF_SCOPE, value);
        }
    }

    @objid ("bc6ed339-b45d-4786-b049-966c4f598c2f")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        /*
         *  add Name property (row = 1)
         */
        table.addProperty(Messages.getString("Ui.Property.Name.Name"), element.getName());
        
        
        /*
         *  add tags properties
         */
        // row=2 -> Severity property
        TaggedValue severityTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
        int minSeverityIndex = TagsManager.getMinSeverityIndex((Class) element);
        table.addProperty (AttackTreeTagTypes.SEVERITY, TagsManager.getTagParameter(severityTag), 
                subArray(TagsManager.SEVERITY_VALUES, minSeverityIndex, TagsManager.SEVERITY_VALUES.length-1)); 
        
        // row=3 -> Probability property
        TaggedValue probabilityTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
        int [] probabilityIndexBounds =  TagsManager.getProbabilityIndexBounds((Class) element);
        table.addProperty (AttackTreeTagTypes.PROBABILITY, TagsManager.getTagParameter(probabilityTag), 
                subArray(TagsManager.PROBABILITY_VALUES, probabilityIndexBounds[0], probabilityIndexBounds[1])); 
        
        // row=4 -> Risk Level Consult property (Read only, because it is calculated automatically based on severity and probability property)
        table.addConsultProperty (AttackTreeTagTypes.RISK_LEVEL, TagsManager.getElementRiskLevel(element));
        
        // row=5 -> Security related
        TaggedValue securityRelatedTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SECURITY_RELATED);
        table.addProperty(AttackTreeTagTypes.SECURITY_RELATED, TagsManager.getTagParameter(securityRelatedTag).equals("true"));
                
        // row=6 -> Safety related
        TaggedValue safetyRelatedTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SAFETY_RELATED);
        table.addProperty(AttackTreeTagTypes.SAFETY_RELATED, TagsManager.getTagParameter(safetyRelatedTag).equals("true"));
        
        // row=7 -> Out of scope
        TaggedValue outOfScopeTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.OUT_OF_SCOPE);
        table.addProperty(AttackTreeTagTypes.OUT_OF_SCOPE, TagsManager.getTagParameter(outOfScopeTag).equals("true"));
    }

    /**
     * Sub array of an array
     * @param array
     * @param indexStart should be superior to 0
     * @param indexEnd should be inferior than array.length
     * @return sub array of array that starts from index indexStart inclusive and ends at indexEnd (inclusive)
     */
    @objid ("3303df11-b591-44b6-8b6c-7274daf26675")
    private static String[] subArray(String[] array, int indexStart, int indexEnd) {
        List<String> list = new ArrayList<>();
        for(int i=indexStart; i <= indexEnd; i++) {
            list.add(array[i]);
        }
        return list.toArray(new String[list.size()]);
    }

}
