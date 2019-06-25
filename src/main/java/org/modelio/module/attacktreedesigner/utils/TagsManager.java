package org.modelio.module.attacktreedesigner.utils;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;

@objid ("0fc43a57-6fcd-465c-ab28-da9a11a0a1d6")
public class TagsManager {
    @objid ("d13e0d68-aa70-4e35-a70b-5974920861dc")
    public static void addParameter(IModelingSession session, Class attackElement, TaggedValue severityTaggedValue, String value) {
        TagParameter tagParameter= session.getModel().createTagParameter(value, severityTaggedValue);
        severityTaggedValue.getActual().add(tagParameter);
    }

    @objid ("8fcc44b6-4c6b-4c39-b517-e5ab4f0ea68c")
    public static TaggedValue createTag(IModelingSession session, String tagName, Class attackElement) {
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

}
