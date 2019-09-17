package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
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
            TagsManager.setTagValue(element, AttackTreeTagTypes.SEVERITY, value);
        
            // calculate Risk Level
            TagsManager.updateRiskLevelTagValue(element);
        
        
        } else if (row == 3) {
            // row=3 -> Probability property
            TagsManager.setTagValue(element, AttackTreeTagTypes.PROBABILITY, value);
        
            // calculate Risk Level
            TagsManager.updateRiskLevelTagValue(element);
        
        } else if (row == 5) {
            // row=5 -> Security related
            TagsManager.setTagValue(element, AttackTreeTagTypes.SECURITY_RELATED, value);
        
        
        } else if (row == 6) {
            // row=6 -> Safety related
            TagsManager.setTagValue(element, AttackTreeTagTypes.SAFETY_RELATED, value);
        
        
        } else if (row == 7) {
            // row=7 -> Out of scope
            TagsManager.setTagValue(element, AttackTreeTagTypes.OUT_OF_SCOPE, value);
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
        table.addProperty (AttackTreeTagTypes.SEVERITY, TagsManager.getParameter(severityTag), TagsManager.SEVERITY_VALUES); 
        
        // row=3 -> Probability property
        TaggedValue probabilityTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
        table.addProperty (AttackTreeTagTypes.PROBABILITY, TagsManager.getParameter(probabilityTag), TagsManager.PROBABILITY_VALUES); 
        
        // row=4 -> Risk Level Consult property (Read only, because it is calculated automatically based on severity and probability property)
        TaggedValue riskLevelTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.RISK_LEVEL);
        table.addConsultProperty (AttackTreeTagTypes.RISK_LEVEL, TagsManager.getParameter(riskLevelTag));
        
        // row=5 -> Security related
        TaggedValue securityRelatedTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SECURITY_RELATED);
        table.addProperty(AttackTreeTagTypes.SECURITY_RELATED, TagsManager.getParameter(securityRelatedTag).equals("true"));
                
        // row=6 -> Safety related
        TaggedValue safetyRelatedTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SAFETY_RELATED);
        table.addProperty(AttackTreeTagTypes.SAFETY_RELATED, TagsManager.getParameter(safetyRelatedTag).equals("true"));
        
        // row=7 -> Out of scope
        TaggedValue outOfScopeTag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.OUT_OF_SCOPE);
        table.addProperty(AttackTreeTagTypes.OUT_OF_SCOPE, TagsManager.getParameter(outOfScopeTag).equals("true"));
        
        
        
        
        
        
        
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

}
