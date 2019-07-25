package org.modelio.module.attacktreedesigner.utils;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("0fc43a57-6fcd-465c-ab28-da9a11a0a1d6")
public class TagsManager {
    @objid ("7f2b0afa-49a3-4cc6-a22e-e521fc637b48")
    public static final String DEFAULT_SEVERITY_VALUE = "MEDIUM";

    @objid ("08d561e8-e7cf-4355-b7dc-93db870463ab")
    public static final String[] SEVERITY_VALUES = {"LOW", "MEDIUM", "HIGH"};

    @objid ("bf4116b1-b72b-466d-a274-4eab254226ed")
    public static final String DEFAULT_PROBABILITY_VALUE = "MEDIUM";

    @objid ("00583923-1051-408e-85c1-113defa75543")
    public static final String DEFAULT_RISK_LEVEL_VALUE = "MEDIUM";

    @objid ("899fe4bf-78f3-4c04-bebb-46b9ee47c858")
    public static final String[] PROBABILITY_VALUES = {"LOW", "MEDIUM", "HIGH"};

    @objid ("93f21aba-0018-45b9-b179-2763e6c5bcf7")
    public static final String DEFAULT_SECURITY_RELATED = "false";

    @objid ("ca8f2286-e9ed-475a-b613-03c15936f7fd")
    public static final String DEFAULT_SAFETY_RELATED = "false";

    @objid ("dca79667-0662-4104-b5e4-e556b10abe03")
    public static final String DEFAULT_OUT_OF_SCOPE = "false";

    @objid ("d13e0d68-aa70-4e35-a70b-5974920861dc")
    public static void addParameter(IModelingSession session, TaggedValue severityTaggedValue, String value) {
        TagParameter tagParameter= session.getModel().createTagParameter(value, severityTaggedValue);
        severityTaggedValue.getActual().add(tagParameter);
    }

    @objid ("8fcc44b6-4c6b-4c39-b517-e5ab4f0ea68c")
    public static TaggedValue createTag(IModelingSession session, String tagName, ModelElement attackElement) {
        TaggedValue severityTaggedValue = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, tagName, attackElement);
        return severityTaggedValue;
    }

    @objid ("350bdc99-632e-4ad6-8431-41eca7841703")
    public static String getParameter(TaggedValue tag, String tagType) {
        String tagName = tag.getDefinition().getName();
        if (tagName.equals(tagType)) {
        
            List<TagParameter> actuals = tag.getActual();
            if ((actuals != null) && (actuals.size() > 0)) {
                return actuals.get(0).getValue();
            } else
                return "";
        
        }
        return "";
    }

    @objid ("5aa945a4-a68e-44a7-ab4e-b65b0df60a86")
    public static void setTagValue(ModelElement element, String tagDefinitionName, String value) {
        List<TaggedValue> listTags = element.getTag();
        for(TaggedValue tag:listTags) {
            if(tag.getDefinition().getName().equals(tagDefinitionName)) {
                List<TagParameter> actuals = tag.getActual();
                for(TagParameter actual:actuals) {
                    actual.setValue(value);
                }
            }
        }
    }

    @objid ("09b4c542-eda9-444e-83ac-2440e15d1c5b")
    public static void createAttackDefaultTags(IModelingSession session, ModelElement attackElement) {
        TaggedValue severityTaggedValue = TagsManager.createTag(session, AttackTreeTagTypes.SEVERITY, attackElement);
        TagsManager.addParameter(session, severityTaggedValue, TagsManager.DEFAULT_SEVERITY_VALUE);
        attackElement.getTag().add(severityTaggedValue);
              
        TaggedValue probabilityTaggedValue = TagsManager.createTag(session,  AttackTreeTagTypes.PROBABILITY, attackElement);
        TagsManager.addParameter(session, probabilityTaggedValue, TagsManager.DEFAULT_PROBABILITY_VALUE);
        attackElement.getTag().add(probabilityTaggedValue);
        
        TaggedValue riskLevelTaggedValue = TagsManager.createTag(session, AttackTreeTagTypes.RISK_LEVEL, attackElement);
        TagsManager.addParameter(session, riskLevelTaggedValue, TagsManager.DEFAULT_RISK_LEVEL_VALUE);
        attackElement.getTag().add(riskLevelTaggedValue);
        
        TaggedValue securityRelated = TagsManager.createTag(session, AttackTreeTagTypes.SECURITY_RELATED, attackElement);
        TagsManager.addParameter(session, securityRelated, TagsManager.DEFAULT_SECURITY_RELATED);
        attackElement.getTag().add(securityRelated);
        
        TaggedValue safetyRelated = TagsManager.createTag(session, AttackTreeTagTypes.SAFETY_RELATED, attackElement);
        TagsManager.addParameter(session, safetyRelated, TagsManager.DEFAULT_SAFETY_RELATED);
        attackElement.getTag().add(safetyRelated);
        
        TaggedValue outOfScope = TagsManager.createTag(session, AttackTreeTagTypes.OUT_OF_SCOPE, attackElement);
        TagsManager.addParameter(session, outOfScope, TagsManager.DEFAULT_OUT_OF_SCOPE);
        attackElement.getTag().add(outOfScope);
    }

    @objid ("265d86ab-a08e-4447-b11d-9b74afdbd53c")
    public static void updateRiskLevelTagValue(MObject selectedElement) {
        String severityValue = "";
        String probabilityValue = "";
        List<TaggedValue> listTags = ((ModelElement) selectedElement).getTag();
        for(TaggedValue tag:listTags) {
        
            if(tag.getDefinition().getName().equals(AttackTreeTagTypes.SEVERITY)) {
                
        
                severityValue = TagsManager.getParameter(tag, AttackTreeTagTypes.SEVERITY);
        
            } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.PROBABILITY)) {
                
        
                probabilityValue = TagsManager.getParameter(tag, AttackTreeTagTypes.PROBABILITY);
        
        
            }
        
        }
        int newRiskLevelOrder = Severity.valueOf(severityValue).ordinal() + Probability.valueOf(probabilityValue).ordinal();
        
        
        if(Severity.valueOf(severityValue).ordinal() > 0 &&  Probability.valueOf(probabilityValue).ordinal() > 0) {
            newRiskLevelOrder++;
        }
        
        String newRiskLevelValue = RiskLevel.values()[newRiskLevelOrder].toString();
        TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.RISK_LEVEL, newRiskLevelValue);
    }

}
