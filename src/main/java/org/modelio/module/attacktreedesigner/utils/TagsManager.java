package org.modelio.module.attacktreedesigner.utils;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;

@objid ("0fc43a57-6fcd-465c-ab28-da9a11a0a1d6")
public class TagsManager {
    @objid ("7f2b0afa-49a3-4cc6-a22e-e521fc637b48")
    public static final String DEFAULT_SEVERITY_VALUE = "MEDIUM";

    @objid ("08d561e8-e7cf-4355-b7dc-93db870463ab")
    public static final String[] SEVERITY_VALUES = {"LOW", "MEDIUM", "HIGH"};

    @objid ("bf4116b1-b72b-466d-a274-4eab254226ed")
    public static final String DEFAULT_PROBABILITY_VALUE = "MEDIUM";

    @objid ("00583923-1051-408e-85c1-113defa75543")
    public static final String DEFAULT_RISK_LEVEL_VALUE = "HIGH";

    @objid ("899fe4bf-78f3-4c04-bebb-46b9ee47c858")
    public static final String[] PROBABILITY_VALUES = {"LOW", "MEDIUM", "HIGH"};

    @objid ("93f21aba-0018-45b9-b179-2763e6c5bcf7")
    public static final String DEFAULT_SECURITY_RELATED = "false";

    @objid ("ca8f2286-e9ed-475a-b613-03c15936f7fd")
    public static final String DEFAULT_SAFETY_RELATED = "false";

    @objid ("dca79667-0662-4104-b5e4-e556b10abe03")
    public static final String DEFAULT_OUT_OF_SCOPE = "false";

    @objid ("350bdc99-632e-4ad6-8431-41eca7841703")
    public static String getTagParameter(TaggedValue tag) {
        List<TagParameter> actuals = tag.getActual();
        if ((actuals != null) && (actuals.size() > 0)) {
            return actuals.get(0).getValue();
        }
        return null;
    }

    @objid ("5aa945a4-a68e-44a7-ab4e-b65b0df60a86")
    public static void setElementTagValue(ModelElement element, String sterotypeName, String tagDefinitionName, String value) {
        TaggedValue tag = element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, sterotypeName, tagDefinitionName);
        if(tag != null) {
            for(TagParameter actual:tag.getActual()) {
                actual.setValue(value);
            }
        }
    }

    @objid ("09b4c542-eda9-444e-83ac-2440e15d1c5b")
    public static void createAttackDefaultTags(IModelingSession session, ModelElement attackElement) {
        TaggedValue severityTag = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeTagTypes.SEVERITY, attackElement);
        //TagsManager.createTag(session, AttackTreeTagTypes.SEVERITY, attackElement);
        TagsManager.createTagParameter(session, severityTag, TagsManager.DEFAULT_SEVERITY_VALUE);
        attackElement.getTag().add(severityTag);
        
        TaggedValue probabilityTag = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeTagTypes.PROBABILITY, attackElement);
        //TagsManager.createTag(session,  AttackTreeTagTypes.PROBABILITY, attackElement);
        TagsManager.createTagParameter(session, probabilityTag, TagsManager.DEFAULT_PROBABILITY_VALUE);
        attackElement.getTag().add(probabilityTag);        
        
        TaggedValue securityRelatedTag = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeTagTypes.SECURITY_RELATED, attackElement);
        //TagsManager.createTag(session, AttackTreeTagTypes.SECURITY_RELATED, attackElement);
        TagsManager.createTagParameter(session, securityRelatedTag, TagsManager.DEFAULT_SECURITY_RELATED);
        attackElement.getTag().add(securityRelatedTag);
        
        TaggedValue safetyRelatedTag = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeTagTypes.SAFETY_RELATED, attackElement);
        //TagsManager.createTag(session, AttackTreeTagTypes.SAFETY_RELATED, attackElement);
        TagsManager.createTagParameter(session, safetyRelatedTag, TagsManager.DEFAULT_SAFETY_RELATED);
        attackElement.getTag().add(safetyRelatedTag);
        
        TaggedValue outOfScopeTag = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeTagTypes.OUT_OF_SCOPE, attackElement);
        //TagsManager.createTag(session, AttackTreeTagTypes.OUT_OF_SCOPE, attackElement);
        TagsManager.createTagParameter(session, outOfScopeTag, TagsManager.DEFAULT_OUT_OF_SCOPE);
        attackElement.getTag().add(outOfScopeTag);
        
        
        /*
         * Create Countered Attack Default Tag
         */
        //TaggedValue counteredAttackTag;
    }

    @objid ("4810aaa4-fd61-433f-9c3f-8b9cb6cebb7c")
    public static void createTagParameter(IModelingSession session, TaggedValue severityTaggedValue, String value) {
        TagParameter tagParameter= session.getModel().createTagParameter(value, severityTaggedValue);
        severityTaggedValue.getActual().add(0,tagParameter);
    }

    @objid ("20bae7fd-cbc6-4f63-98d7-5c4ee875dfc0")
    public static String getElementTagParameter(ModelElement element, String sterotypeName, String tagDefinitionName) {
        TaggedValue tag =  element.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, sterotypeName, tagDefinitionName);
        if(tag != null) {
            return getTagParameter(tag);
        } else {
            return null;
        }
    }

    @objid ("3dedc5fd-4427-4344-bb9a-68f1e8367a0b")
    public static String getElementRiskLevel(ModelElement element) {
        String severityValue = TagsManager.getElementTagParameter(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
        String probabilityValue = TagsManager.getElementTagParameter(element, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
        
        if(severityValue != null && probabilityValue != null) {
            int newRiskLevelOrder = Severity.valueOf(severityValue).ordinal() + Probability.valueOf(probabilityValue).ordinal();
            
            if(Severity.valueOf(severityValue).ordinal() > 0 &&  Probability.valueOf(probabilityValue).ordinal() > 0) 
                newRiskLevelOrder++;
            
            return RiskLevel.values()[newRiskLevelOrder].toString();
        }
        return null;
    }

}
