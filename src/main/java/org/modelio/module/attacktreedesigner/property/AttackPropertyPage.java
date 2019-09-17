package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.Probability;
import org.modelio.module.attacktreedesigner.utils.RiskLevel;
import org.modelio.module.attacktreedesigner.utils.Severity;
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
        
            // calculate Risk Level
            updateRiskLevelTagValue(element);
        
        
        } else if (row == 3) {
            // row=3 -> Probability property
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY, value);
        
            // calculate Risk Level
            updateRiskLevelTagValue(element);
        
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
        table.addProperty (AttackTreeTagTypes.SEVERITY, TagsManager.getTagParameter(severityTag), TagsManager.SEVERITY_VALUES); 
        
        // row=3 -> Probability property
        TaggedValue probabilityTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
        table.addProperty (AttackTreeTagTypes.PROBABILITY, TagsManager.getTagParameter(probabilityTag), TagsManager.PROBABILITY_VALUES); 
        
        // row=4 -> Risk Level Consult property (Read only, because it is calculated automatically based on severity and probability property)
        TaggedValue riskLevelTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.RISK_LEVEL);
        table.addConsultProperty (AttackTreeTagTypes.RISK_LEVEL, TagsManager.getTagParameter(riskLevelTag));
        
        // row=5 -> Security related
        TaggedValue securityRelatedTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SECURITY_RELATED);
        table.addProperty(AttackTreeTagTypes.SECURITY_RELATED, TagsManager.getTagParameter(securityRelatedTag).equals("true"));
                
        // row=6 -> Safety related
        TaggedValue safetyRelatedTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SAFETY_RELATED);
        table.addProperty(AttackTreeTagTypes.SAFETY_RELATED, TagsManager.getTagParameter(safetyRelatedTag).equals("true"));
        
        // row=7 -> Out of scope
        TaggedValue outOfScopeTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.OUT_OF_SCOPE);
        table.addProperty(AttackTreeTagTypes.OUT_OF_SCOPE, TagsManager.getTagParameter(outOfScopeTag).equals("true"));
        
        
        
        
        
        
        
        //List<TaggedValue> listTags = element.getTag();
        
        //        for(TaggedValue tag : listTags) {
        //        
        //            if(tag.getDefinition().getName().equals(AttackTreeTagTypes.SEVERITY)) {
        //        
        //                table.addProperty (tag.getDefinition().getName(), 
        //                        TagsManager.getParameter(tag
        //                                //, tag.getDefinition().getName()
        //                                ),
        //                        TagsManager.SEVERITY_VALUES);
        //        
        //            } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.PROBABILITY)){
        //                table.addProperty (tag.getDefinition().getName(), 
        //                        TagsManager.getParameter(tag
        //                                //, tag.getDefinition().getName()
        //                                ),
        //                        TagsManager.PROBABILITY_VALUES);
        //        
        //            } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.RISK_LEVEL)) {
        //                table.addConsultProperty (tag.getDefinition().getName(), 
        //                        TagsManager.getParameter(tag
        //                                //, tag.getDefinition().getName()
        //                                ));
        //        
        //            } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.SECURITY_RELATED)) {
        //                table.addProperty(tag.getDefinition().getName(), 
        //                        TagsManager.getParameter(tag
        //                                //, tag.getDefinition().getName()
        //                                ).equals("true"));
        //        
        //            } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.SAFETY_RELATED)) {
        //                table.addProperty(tag.getDefinition().getName(), 
        //                        TagsManager.getParameter(tag
        //                                //, tag.getDefinition().getName()
        //                                ).equals("true"));
        //        
        //            } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.OUT_OF_SCOPE)) {
        //                table.addProperty(tag.getDefinition().getName(), 
        //                        TagsManager.getParameter(tag
        //                                //, tag.getDefinition().getName()
        //                                ).equals("true"));
        //            }
        //            
        //            // custom tags
        //        //            else {
        //        //                table.addConsultProperty (tag.getDefinition().getName(), 
        //        //                        TagsManager.getParameter(tag, tag.getDefinition().getName()));
        //        //            }
        //        }
        
        
        /*
         * add counter measures
         */
        //        List<Note> attackNotes = element.getDescriptor();
        //        for(Note attackNote:attackNotes) {
        //            if(attackNote.getModel().getName().equals(AttackTreeNoteTypes.COUNTER_MEASURE)) {
        //                table.addConsultProperty(AttackTreeNoteTypes.COUNTER_MEASURE, attackNote.getContent());
        //            }
        //        }
    }

    @objid ("c3d68873-7052-4395-ae47-9d981f51afd8")
    private static void updateRiskLevelTagValue(ModelElement element) {
        String severityValue = TagsManager.getElementTagParameter(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
        String probabilityValue = TagsManager.getElementTagParameter(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
        
        if(severityValue != null && probabilityValue != null) {
            int newRiskLevelOrder = Severity.valueOf(severityValue).ordinal() + Probability.valueOf(probabilityValue).ordinal();
            
            if(Severity.valueOf(severityValue).ordinal() > 0 &&  Probability.valueOf(probabilityValue).ordinal() > 0) 
                newRiskLevelOrder++;
            
            String newRiskLevelValue = RiskLevel.values()[newRiskLevelOrder].toString();
            TagsManager.setElementTagValue(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.RISK_LEVEL, newRiskLevelValue);
        }
    }

}
